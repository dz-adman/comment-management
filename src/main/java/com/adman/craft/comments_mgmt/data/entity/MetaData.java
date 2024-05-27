package com.adman.craft.comments_mgmt.data.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.BatchSize;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "metadata")
public class MetaData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(AccessLevel.NONE)
    private Long id;

    @Builder.Default
    private int likes = 0;

    @Builder.Default
    private int dislikes = 0;

    @OneToMany(fetch = FetchType.LAZY)
    @BatchSize(size = 50)
    private List<SocialUser> likedByUsers;

    @OneToMany(fetch = FetchType.LAZY)
    @BatchSize(size = 50)
    private List<SocialUser> dislikedByUsers;

    @Column(nullable = false)
    private LocalDateTime postedAt;

    private LocalDateTime lastUpdateAt;

    @Version
    private Long version;

    public void addLike(SocialUser user) {
        this.likes += 1;
        if (Objects.isNull(this.likedByUsers)) {
            this.likedByUsers = new ArrayList<>();
        }
        this.likedByUsers.add(user);
        if (!Objects.isNull(this.dislikedByUsers) && this.dislikedByUsers.contains(user)) {
            this.dislikes = Math.max(0, this.dislikes - 1);
            this.dislikedByUsers.remove(user);
        }
    }

    public void removeLike(SocialUser user) {
        if (this.getLikedByUsers().contains(user)) {
            this.likes = Math.max(0, this.likes - 1);
            this.getLikedByUsers().remove(user);
        }
    }

    public void addDislike(SocialUser user) {
        this.dislikes += 1;
        if (Objects.isNull(this.dislikedByUsers)) {
            this.dislikedByUsers = new ArrayList<>();
        }
        this.getDislikedByUsers().add(user);
        if (!Objects.isNull(this.likedByUsers) && this.likedByUsers.contains(user)) {
            this.likes = Math.max(0, this.likes - 1);
            this.likedByUsers.remove(user);
        }
    }

    public void removeDislike(SocialUser user) {
        if (this.getDislikedByUsers().contains(user)) {
            this.dislikes = Math.max(0, this.dislikes - 1);
            this.getDislikedByUsers().remove(user);
        }
    }
}
