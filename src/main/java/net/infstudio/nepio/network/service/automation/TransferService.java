package net.infstudio.nepio.network.service.automation;

import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.fabricmc.fabric.api.transfer.v1.item.ItemVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.StorageUtil;
import net.fabricmc.fabric.api.transfer.v1.storage.TransferVariant;
import net.fabricmc.fabric.api.transfer.v1.transaction.Transaction;
import net.infstudio.nepio.network.NNetwork;
import net.infstudio.nepio.network.api.IComponent;
import net.infstudio.nepio.network.api.automation.*;
import net.infstudio.nepio.network.service.HandlerService;
import net.infstudio.nepio.util.PriorityBucket;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Item/Fluid transportation service.
 */
public class TransferService<T extends TransferVariant> extends HandlerService<TransferService<T>.TransferPayload> {

    public static final TransferService<ItemVariant> ITEM_INSTANCE = new TransferService<>(ItemVariant.class);
    public static final TransferService<FluidVariant> FLUID_INSTANCE = new TransferService<>(FluidVariant.class);

    private final Class<T> clazz;

    public TransferService(Class<T> clazz) {
        this.clazz = clazz;
    }

    public void tick() {
        for (NNetwork network : payloads.keySet()) {
            doTransfer(network);
        }
    }

    private void doTransfer(NNetwork network) {
        TransferPayload payload = payloads.get(network);
        if (payload.tick < 20) {
            payload.tick++;
            return;
        }
        payload.tick = 0;

        var insertableList =
                payload.insertableList.stream().filter(insertable -> insertable.isEnabled() && insertable.getStorage() != null).collect(Collectors.toList());
        var extractableList =
                payload.extractableList.stream().filter(extractable -> extractable.isEnabled() && extractable.getStorage() != null).collect(Collectors.toList());

        PriorityBucket<Integer, IInsertable<T>> insertableBucket =
                new PriorityBucket<>(insertableList, Comparator.reverseOrder(), Comparator.comparing(IInsertable::getPriority), IMovable::getPriority);
        PriorityBucket<Integer, IExtractable<T>> extractableBucket =
                new PriorityBucket<>(extractableList, Comparator.reverseOrder(), Comparator.comparing(IExtractable::getPriority), IMovable::getPriority);

        long maxAmount = 1;

        for (int insertPriority : insertableBucket.getBuckets()) {
            insertableList = insertableBucket.getBucketContent(insertPriority);
            Collections.shuffle(insertableList);
            for (IInsertable<T> insertable : insertableList) {
                for (int extractPriority : extractableBucket.getBuckets()) {
                    extractableList = extractableBucket.getBucketContent(extractPriority);
                    Collections.shuffle(extractableList);
                    for (IExtractable<T> extractable : extractableList) {
                        try (Transaction transaction = Transaction.openOuter()) {
                            maxAmount -= StorageUtil.move(insertable.getStorage(), extractable.getStorage(), insertable.getFilter().and(extractable.getFilter()),
                                maxAmount, transaction);
                            transaction.commit();
                        }
                        if (maxAmount == 0) break;
                    }
                }
                if (maxAmount == 0) break;
            }
            if (maxAmount == 0) break;
        }

    }

    @Override
    protected TransferPayload createPayload() {
        return new TransferPayload();
    }

    protected class TransferPayload extends NetworkPayload {

        public Set<IInsertable<T>> insertableList;
        public Set<IExtractable<T>> extractableList;

        public TransferPayload() {
            insertableList = new HashSet<>();
            extractableList = new HashSet<>();
        }

        @Override
        public void addComponent(IComponent component) {
            if (component instanceof IInsertable insertable) {
                if (insertable.get() == clazz) {
                    insertableList.add(insertable);
                }
            } else if (component instanceof IExtractable extractable) {
                if (extractable.get() == clazz) {
                    extractableList.add(extractable);
                }
            }
        }

        @Override
        public void removeComponent(IComponent component) {
            if (component instanceof IInsertable insertable) {
                if (insertable.get() == clazz) {
                    insertableList.remove(insertable);
                }
            } else if (component instanceof IExtractable extractable) {
                if (extractable.get() == clazz) {
                    extractableList.remove(extractable);
                }
            }
        }

    }

}


