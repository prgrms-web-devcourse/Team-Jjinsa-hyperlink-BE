package com.hyperlink.server.member.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.hyperlink.server.domain.auth.token.AuthTokenExtractor;
import com.hyperlink.server.domain.auth.token.JwtTokenProvider;
import com.hyperlink.server.domain.auth.token.RefreshTokenRepository;
import com.hyperlink.server.domain.category.domain.CategoryRepository;
import com.hyperlink.server.domain.category.domain.entity.Category;
import com.hyperlink.server.domain.member.application.MemberService;
import com.hyperlink.server.domain.member.domain.Career;
import com.hyperlink.server.domain.member.domain.CareerYear;
import com.hyperlink.server.domain.member.domain.MemberRepository;
import com.hyperlink.server.domain.member.domain.entity.Member;
import com.hyperlink.server.domain.member.dto.MyPageResponse;
import com.hyperlink.server.domain.member.dto.SignUpRequest;
import com.hyperlink.server.domain.member.dto.SignUpResult;
import com.hyperlink.server.domain.member.exception.MemberNotFoundException;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@SpringBootTest
class MemberServiceIntegrationTest {

  @Autowired
  private MemberService memberService;

  @Autowired
  private MemberRepository memberRepository;

  @Autowired
  private CategoryRepository categoryRepository;

  @Autowired
  private RefreshTokenRepository refreshTokenRepository;

  @Autowired
  private AuthTokenExtractor authTokenExtractor;

  @Autowired
  private JwtTokenProvider jwtTokenProvider;

  @DisplayName("주어진 이메일정보로 가입 멤버 존재여부를 확인할 수 있다.")
  @Test
  void existsMemberByEmailTest() {
    Member saveMember = memberRepository.save(
        new Member("rldnd1234@naver.com", "Chocho", Career.DEVELOP, CareerYear.MORE_THAN_TEN,
            "localhost", 1995, "man"));

    assertThat(memberService.existsMemberByEmail(saveMember.getEmail())).isTrue();
    assertThat(memberService.existsMemberByEmail("rldnd")).isFalse();
  }

  @DisplayName("회원가입을 할 수 있다.")
  @Test
  void signUpTest() {
    Category beauty = categoryRepository.save(new Category("beauty"));

    SignUpRequest signUpRequest = new SignUpRequest("rldnd1234@naver.com", "Chocho", "develop",
        "ten", 1995, List.of("develop", "beauty"), "man");

    SignUpResult signUpResult = memberService.signUp(signUpRequest, "profileUrl");

    assertThat(memberRepository.existsById(signUpResult.memberId())).isTrue();
    assertThat(refreshTokenRepository.existsById(signUpResult.refreshToken())).isTrue();
    assertThat(authTokenExtractor.extractMemberId(signUpResult.accessToken()).get()).isEqualTo(
        signUpResult.memberId());
  }

  @DisplayName("회원의 정보를 전달할 수 있다.")
  @Test
  void myPageCorrectTest() {
    Member saveMember = memberRepository.save(
        new Member("rldnd1234@naver.com", "Chocho", Career.DEVELOP, CareerYear.MORE_THAN_TEN,
            "localhost", 1995, "man"));
    MyPageResponse myPageResponse = memberService.myInfo(saveMember.getId());

    assertThat(myPageResponse.email()).isEqualTo(saveMember.getEmail());
    assertThat(myPageResponse.nickname()).isEqualTo(saveMember.getNickname());
    assertThat(myPageResponse.career()).isEqualTo(saveMember.getCareer().getValue());
    assertThat(myPageResponse.careerYear()).isEqualTo(saveMember.getCareerYear().getValue());
    assertThat(myPageResponse.profileUrl()).isEqualTo(saveMember.getProfileImgUrl());
  }

  @DisplayName("회원의 정보 전달시 member정보가 존재하지 않는 회원이라면 MemberNotFoundException을 던진다.")
  @Test
  void myPageInCorrectTest() {
    assertThatThrownBy(() -> memberService.myInfo(1L)).isInstanceOf(MemberNotFoundException.class);
  }
}