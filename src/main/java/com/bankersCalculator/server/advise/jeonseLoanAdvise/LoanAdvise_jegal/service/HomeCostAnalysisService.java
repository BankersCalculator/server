package com.bankersCalculator.server.advise.jeonseLoanAdvise.LoanAdvise_jegal.service;

import com.bankersCalculator.server.advise.jeonseLoanAdvise.LoanAdvise_jegal.product.JeonseProductImpl.JeonseProduct;

/*
(자료수집)
A. 사용자 pain point (네이버 지식인 질문 수집)
1) LH청년전세대출 질문, 청약에 당첨이 되어야지 받을 수 있는 대출인가요? 그냥 일반 오피스텔이나 주택 전세자금은 은행 전세자금대출을 받으면 되는거죠??
2) 집에 융자가 있는 집인데, 전세끼고 매매하시는거같아요전세로 이사를하려고하는데 안심전세대출이 가능한집이면 안전할까요?
3) 청년 버팀목전세대출로 매물 알아보고 있는데, 공동주택 / 빌라는 버팀목전세대출에 해당되는지 궁금합니다
4) 이번에 혼인신고하고 신혼부부전세 대출보는데 남편이 일을하고있고 저는 일을 쉬는중이라 전 수익이 0원 입니다 그런데 제가 신용도가 높아서 저의 명의로 대출을 받으려하는데요 보니 수익이없으면 대출이 안되더라구요? 남편은 수익이 있고 저는 없는데 그럼 남편 명의로 대출을 해야하나요?
5) 전세대출 알아보고있는많이 나오는줄 알았더니 최대 한도가 1억2천 이정도로 나오더라구요. 2억~2억 4천 정도까지 나오는 전세대출도 있을까요? 무주택 미혼 자녀없는 개인 입니다

(참고)
A. 집지켜 사용자 시나리오
 집주소 입력 / 상세주소입력 > 전월세 보증금입력 / 거주유무 > 리포트확인 (카카오로그인) > 집정보를 가지고오고있어요 (등기부발급 700원내줌) >
 리포트요약: 보증금수정기능, 대출알아보기, 등기부변동알림, 확정일자받, 주의사항, 원본문서, 집주인정보, 집주인과거, 보증보험, 내집이경매로넘어간다면  >
 > 대출알아보기 : 주택소유정보 / 연소득 / 대출보유여부, 대출종류(모두선택) / 빚제외 총자산규모 / 만나이 / 해당내용 모두선택 (자녀,신혼,미혼/외벌이,중소기업/청년창업, 공공임대거주, 연체부도이력, 혁신도시이전공공기관종사자, 재개발지역입주, 2년내입양출산유무)
            / 불러온정보확인(보증금, 집크기, 지역(수도권유무))
 > 가능대출결과 및 대출별 충족여부 > 필터에 걸리면 종료 > 충족가능하면, 집주소선택 > 가능대출목록제공 > 대출상세내용 (이자계산기, 보증보험가입여부확인, 주의사항멘트, 취급가능은행)

(input데이터)
A. 고객 정보
1) 만나이
2) 혼인여부(기혼/미혼/예정)
3) 신혼여부 선택
4) 주택보유수
5) 연소득, 배우자연소득
6) 자녀여부(1자녀/2자녀), 신생아여부, 중소기업재직여부
7) 만기/상환방식

B. 주택 정보
1) 임차보증금/ 월세
2) 주택타입(아파트/오피스텔/다가구단독/연립다세대)
3) 임차전용면적(85m2이하/초과 등)
4) 위치(서울/경기/인천/수도권외)
5) 필요금액 - 직접입력 혹은 최대한도

(스켈레톤 구현)
A. HomeCostAnalysisService.java : 주거비용분석서비스.
 케이스 a) 전세 1개 투입 (전세대출추천)
 케이스 b) 전세 2개 투입 (전세대출추천, 비교)
 케이드 c) 전세, 월세 각 1개투입 (전세대출추천, 비교)
   가. 전월세 단순비용 계산
     - 이자금액
     - 부수비용 (보증료, 인지세 등)

   나. 전세 대비 월세비교우위
     - 현금확보가능 : 해당 금액 예금넣을 경우의 기회비용 ( 5000만원 * 3% = 106,750원 )
     - 장점들에 대한 줄글알림

   다. 정성적 비교 (주거면적/내용연수/관리비/거주환경 비교)
 @param 사용자입력정보 (기본정보, 주택정보), 케이스별(a, b, c)
 @return 단순비용계산, 전세대비월세비교우위, 정성적비교

B. JeonseLoanAdvisorService.java : 전세대출추천서비스.
 @ param 사용자입력정보 (기본정보, 주택정보)
 @ return ProductFilteringService에서 생성된(상품분류, 상품코드, 상품명), LoanLimitCostEstimatorService에서 생성된 (필요기준 금액/이자율/매월발생이자), ProductSelectionService에서 생성된(선정기준, 순위)

 B-1) ProductFilteringService.java : 입력정보를 바탕으로 충족/미충족 여부를 판단. 충족/미충족 여부에 대해 반환 (입력정보와 상품팩토리의 상품별 요구조건 비교 후 그 결과를 반환)
   @param 사용자입력값 (고객정보, 주택정보)
   @method 사용자입력값과 JeonseProductFactory에 기입력된 JeonseProduct 리스트 전체와 비교하여 가능한 상품을 필터링함.
   @return 상품분류, 상품코드, 상품명, 가능여부, 취급은행 (ex : JUNSE, HF0001, 신생아특버팀목전제자금대출, Y, {하나,신한,우리})
   ex) A의 입력정보를 바탕으로 가능한 상품은 가,나,다 입니다. 라, 마는 z 사유에 의해 불가합니다.

 B-2) LoanLimitCostEstimatorService.java,  대출한도/비용산출 : 필터링 통과한 상품의 한도 및 비용산출
    @param  ProductFilteringService의 method를 통해 필터링된 상품리스트들, 사용자입력정보 (기본정보, 주택정보)
    @method 필터링된 상품리스트들, 사용자입력정보를 바탕으로 JeonseProductFactory에 기입력된 JeonseProduct의 대출한도 산정식을 통해 return 값을 산정함.
    @return 최대가능기준 금액/이자율/매월발생이자,  필요기준 금액/이자율/매월발생이자, 발생비용

 B-3) ProductSelectionService.java, 상품선정 (해당 부분의 고도화는 추후 생각하는 것으로하고 최대한도, 최저금리 기준으로만 선정)
    가. 최대한도기준
    나. 최저금리기준
    다. 이용가능연수 기준

    최대한도기준 -  1순위, 최저금리기준 - 1순위 총 2건의 상품 최종선정
    @param LoanLimitCostEstimatorService의 method를 통해 생성된 각 상품의 정보들
    @method 위의 각 기준에 따라 상품의 순위를 메김.
    @return 순위, 선정기준

C. JeonseProductFactory.java :
   > 각 상품별 필요조건 : 상품분류, 상품코드, 상품명, 항목코드, 항목설명, 기준값
                     (JUNSE, HF0001, 신생아특버팀목전제자금대출, A0001, 연소득, 5000)
   > 각 상품별 한도산출 : 목적물별보증한도, 소요자금별보증한도, 상환능력별보증한도, 보증종류별보증한도

   > 각 상품별 이자산출 : ...
   > 각 상품별 비용산출 : ...
D. JeonseProduct.java (인터페이스)
   1) 상품별 필요조건 변수
   2) 상품별 한도산출 메소드
   3) 상품별 이자산출 메소드
   4) 상품별 비용산출 메소드

E. JeonseProductImpl 디렉토리



-> 각각의 서비스 호출시에 다 기능이 단독으로도 작동하게끔할 것.
손님 1) 특정상품의 대출한도/비용만 알고싶은고객 (LoanLimitCostEstimatorService)
손님 2) A, B  상품만을 비교하고싶은 고객 (ProductSelectionService)
손님 3) 나한테 맞는 상품과 취급은행만 알고싶은 고객 (ProductFilteringService)
손님 4) 본인에게 맞는 최적의 전세대출 추천을 바라는 고객 (JeonseLoanAdvisorService)
손님 5) 전/월세의 비용 분석을 원하는 고객 (HomeCostAnalysisService)


 */
public class HomeCostAnalysisService {
    public Map<String, Object> analyzeCost(CustomerInfo customerInfo, HousingInfo housingInfo, String caseType) {
        // 전월세 단순비용 계산
        // 전세 대비 월세 비교우위 계산
        // 정성적 비교
        return null;
    }
}
