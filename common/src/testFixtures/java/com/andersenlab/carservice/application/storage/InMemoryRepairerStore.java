package com.andersenlab.carservice.application.storage;

import com.andersenlab.Comparators;
import com.andersenlab.carservice.port.external.RepairerStore;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public final class InMemoryRepairerStore implements RepairerStore {

    private final Map<UUID, RepairerEntity> map = new HashMap<>();
    private final Comparators comparators = Comparators.create();

    @Override
    public void save(RepairerEntity repairerEntity) {
        map.put(repairerEntity.id(), repairerEntity);
    }

    @Override
    public Collection<RepairerEntity> findAllSorted(Sort sort) {
        return map.values().stream()
                .sorted(comparators.comparator(sort))
                .toList();
    }

    @Override
    public void delete(UUID id) {
        map.remove(id);
    }

    @Override
    public boolean has(UUID id) {
        return map.containsKey(id);
    }

    @Override
    public boolean hasRepairerWithStatusAssigned(UUID id) {
        if (!has(id)) {
            return false;
        }
        return map.get(id).status() == RepairerStatus.ASSIGNED;
    }

    @Override
    public RepairerEntity getById(UUID id) {
        return map.get(id);
    }
}
