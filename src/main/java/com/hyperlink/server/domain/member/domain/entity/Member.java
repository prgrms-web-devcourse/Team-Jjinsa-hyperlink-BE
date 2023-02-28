package com.hyperlink.server.domain.member.domain.entity;

import com.hyperlink.server.domain.common.BaseEntity;
import com.hyperlink.server.domain.company.domain.entity.Company;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "member_id")
  private Long id;

  @Column(nullable = false, unique = true)
  private String email;

  @Column(length = 30, nullable = false)
  private String nickname;

  @Column(length = 30, nullable = false)
  private String career;

  @Column(length = 30, nullable = false)
  private String careerYear;

  @Column(nullable = false)
  private String profileImgUrl;

  @Column(columnDefinition = "INT UNSIGNED")
  private Integer birthYear;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "company_id")
  private Company company;

  public Member(String email, String nickname, String career, String careerYear,
      String profileImgUrl) {
    this.email = email;
    this.nickname = nickname;
    this.career = career;
    this.careerYear = careerYear;
    this.profileImgUrl = profileImgUrl;
  }

  public Member(String email, String nickname, String career, String careerYear,
      String profileImgUrl, Integer birthYear) {
    this.email = email;
    this.nickname = nickname;
    this.career = career;
    this.careerYear = careerYear;
    this.profileImgUrl = profileImgUrl;
    this.birthYear = birthYear;
  }
}
