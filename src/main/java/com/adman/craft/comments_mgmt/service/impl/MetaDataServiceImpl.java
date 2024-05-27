package com.adman.craft.comments_mgmt.service.impl;

import com.adman.craft.comments_mgmt.data.entity.MetaData;
import com.adman.craft.comments_mgmt.service.MetaDataService;
import com.adman.craft.comments_mgmt.data.repository.MetaDataRepository;
import jakarta.annotation.Nonnull;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class MetaDataServiceImpl implements MetaDataService {

    private final MetaDataRepository metaDataRepository;

    @Transactional
    public @NonNull MetaData addEmptyMetaData() {
        return metaDataRepository.save(MetaData.builder()
                .likes(0)
                .dislikes(0)
                .likedByUsers(new ArrayList<>())
                .dislikedByUsers(new ArrayList<>())
                .postedAt(LocalDateTime.now())
                .build());
    }

    public @NonNull MetaData addMetaData() {
        return metaDataRepository.save(MetaData.builder()
                .likes(0)
                .dislikes(0)
                .dislikedByUsers(new ArrayList<>())
                .dislikedByUsers(new ArrayList<>())
                .postedAt(LocalDateTime.now())
                .build());
    }

    public @NonNull Optional<MetaData> getMetaData(@NonNull Long id) {
        return metaDataRepository.findById(id);
    }

    public @NonNull MetaData saveOrUpdateMetaData(@NonNull MetaData metaData) {
        return metaDataRepository.save(metaData);
    }

    public void deleteMetaData(@Nonnull final Long id) {
        metaDataRepository.deleteById(id);
    }

}
