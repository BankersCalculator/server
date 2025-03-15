package com.myZipPlan.server.user.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GuestUsageRedisRepository extends CrudRepository<GuestUsage, Long> {

    GuestUsage findByGuestId(String guestId);

    default GuestUsage incrementCount(String guestId) {
        GuestUsage guestUsage = findByGuestId(guestId);
        if (guestUsage != null) {
            guestUsage.setCount(guestUsage.getCount() + 1);
            save(guestUsage);
            return guestUsage;
        } else {
            GuestUsage newGuestUsage = GuestUsage.builder()
                .guestId(guestId)
                .count(1)
                .build();
            save(newGuestUsage);
            return newGuestUsage;
        }
    }
}
