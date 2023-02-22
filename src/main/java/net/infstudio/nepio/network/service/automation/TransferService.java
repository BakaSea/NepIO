package net.infstudio.nepio.network.service.automation;

import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.fabricmc.fabric.api.transfer.v1.item.ItemVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.Storage;
import net.fabricmc.fabric.api.transfer.v1.storage.StorageUtil;
import net.fabricmc.fabric.api.transfer.v1.storage.StorageView;
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
        if (payload.tick < 5) {
            payload.tick++;
            return;
        }
        payload.tick = 0;

        var insertableList =
                payload.insertableList.stream().filter(insertable -> insertable.isEnabled() && insertable.getStorage() != null).toList();
        var extractableList =
                payload.extractableList.stream().filter(extractable -> extractable.isEnabled() && extractable.getStorage() != null).toList();

        PriorityBucket<Integer, IInsertable<T>> insertableBucket =
                new PriorityBucket<>(insertableList, Comparator.reverseOrder(), Comparator.comparing(IInsertable::getPriority), IMovable::getPriority);
        PriorityBucket<Integer, IExtractable<T>> extractableBucket =
                new PriorityBucket<>(extractableList, Comparator.reverseOrder(), Comparator.comparing(IExtractable::getPriority), IMovable::getPriority);

        for (int insertPriority : insertableBucket.getBuckets()) {
            insertableList = insertableBucket.getBucketContent(insertPriority);
            Collections.shuffle(insertableList);
            for (IInsertable<T> insertable : insertableList) {
                for (int extractPriority : extractableBucket.getBuckets()) {
                    extractableList = extractableBucket.getBucketContent(extractPriority);
                    long total = 0;
                    switch (insertable.getMode()) {
                        case RANDOM: {
                            List<Integer> idx = new ArrayList<>();
                            for (int i = 0; i < extractableList.size(); ++i) idx.add(i);
                            Collections.shuffle(idx);
                            for (int i : idx) {
                                IExtractable<T> extractable = extractableList.get(i);
                                try (Transaction transaction = Transaction.openOuter()) {
                                    total += StorageUtil.move(insertable.getStorage(), extractable.getStorage(), insertable.getFilter().and(extractable.getFilter()),
                                            Math.min(insertable.getSpeed() - total, extractable.getSpeed()), transaction);
                                    transaction.commit();
                                }
                                if (total == insertable.getSpeed()) break;
                            }
                            break;
                        }
                        case SPLIT, FORCE_SPLIT: {
                            try (Transaction transaction = Transaction.openOuter()) {
                                for (StorageView<T> view : insertable.getStorage().iterable(transaction)) {
                                    if (view.isResourceBlank()) continue;
                                    T resource = view.getResource();
                                    if (!insertable.getFilter().test(resource)) continue;
                                    long amount = 0;
                                    try (Transaction extractionTestTransaction = transaction.openNested()) {
                                        amount = view.extract(resource, insertable.getSpeed() - total, extractionTestTransaction);
                                        extractionTestTransaction.abort();
                                    }
                                    if (amount == 0) break;
                                    var acceptableList = extractableList.stream().filter(extractable -> extractable.getFilter().test(resource)).toList();
                                    amount /= acceptableList.size();
                                    if (insertable.getMode() == IInsertable.Mode.SPLIT) {
                                        for (IExtractable<T> acceptable : acceptableList) {
                                            try (Transaction insertionTestTransaction = transaction.openNested()) {
                                                amount = Math.min(amount, acceptable.getStorage().insert(resource, acceptable.getSpeed(), insertionTestTransaction));
                                                insertionTestTransaction.abort();
                                            }
                                        }
                                    }
                                    long cnt = 0;
                                    for (IExtractable<T> acceptable : acceptableList) {
                                        cnt += acceptable.getStorage().insert(resource, amount, transaction);
                                    }
                                    insertable.getStorage().extract(resource, cnt, transaction);
                                    total += cnt;
                                }
                                transaction.commit();
                            }
                            break;
                        }
                        case ROUND_ROBIN: {

                            break;
                        }
                    }
                    if (total > 0) break;
                }
            }
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


