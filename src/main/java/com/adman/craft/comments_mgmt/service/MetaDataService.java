package com.adman.craft.comments_mgmt.service;

import com.adman.craft.comments_mgmt.data.entity.MetaData;
import jakarta.annotation.Nonnull;
import lombok.NonNull;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public interface MetaDataService {

    @NonNull
    MetaData addEmptyMetaData();

    @NonNull
    MetaData addMetaData();

    @NonNull
    Optional<MetaData> getMetaData(@NonNull Long id);

    @NonNull
    MetaData saveOrUpdateMetaData(@NonNull MetaData metaData);

    void deleteMetaData(@Nonnull final Long id);
}