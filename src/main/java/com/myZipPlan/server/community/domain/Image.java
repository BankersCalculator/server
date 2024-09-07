package com.myZipPlan.server.community.domain;

import jakarta.persistence.*;

@Entity
public class Image {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String url; // S3에 저장된 이미지 경로

    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post post;
}
