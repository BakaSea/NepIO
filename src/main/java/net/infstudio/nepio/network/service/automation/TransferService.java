package net.infstudio.nepio.network.service.automation;

import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.fabricmc.fabric.api.transfer.v1.item.ItemVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.StorageUtil;
import net.fabricmc.fabric.api.transfer.v1.storage.TransferVariant;
import net.fabricmc.fabric.api.transfer.v1.transaction.Transaction;
import net.infstudio.nepio.network.NNetwork;
import net.infstudio.nepio.network.NNetworkNode;
import net.infstudio.nepio.network.api.IComponent;
import net.infstudio.nepio.network.api.automation.*;
import net.infstudio.nepio.network.service.NetworkService;
import net.infstudio.nepio.util.PriorityBucket;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class TransferService<T extends TransferVariant> {

    public static final TransferService<ItemVariant> ITEM_INSTANCE = new TransferService<>(ItemVariant.class);
    public static final TransferService<FluidVariant> FLUID_INSTANCE = new TransferService<>(FluidVariant.class);

    private Class<T> clazz;

    public TransferService(Class<T> clazz) {
        this.clazz = clazz;
    }

    public void doTransfer() {
        for (NNetwork network : NetworkService.INSTANCE.getNetworks()) {
            doTransfer(network);
        }
    }

    private void doTransfer(NNetwork network) {
        List<IInsertable<T>> insertableList = new ArrayList<>();
        List<IExtractable<T>> extractableList = new ArrayList<>();

        for (NNetworkNode node : network.getNodes()) {
            for (IComponent component : node.getComponents()) {
                if (component instanceof IInsertable insertable) {
                    if (insertable.get().equals(clazz)) {
                        insertableList.add(insertable);
                    }
                } else if (component instanceof IExtractable extractable) {
                    if (extractable.get().equals(clazz)) {
                        extractableList.add(extractable);
                    }
                }
            }
        }

        insertableList = insertableList.stream().filter(insertable -> insertable.isEnabled() && insertable.getStorage() != null).collect(Collectors.toList());
        extractableList = extractableList.stream().filter(extractable -> extractable.isEnabled() && extractable.getStorage() != null).collect(Collectors.toList());

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

}
