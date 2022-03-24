package jp.coupon.archerhistory.bean;

import java.util.List;

import jp.coupon.archerhistory.entity.TblScoreEntity;


public class HistoryInitDataBean {

    /** 集計ラウンド数 */
    String sumRoundNum;

    /** 日付リスト */
    List<String> lstDate;

    /** 距離リスト */
    List<String> lstDistance;

    /** 初期スコアリスト */
    List<TblScoreEntity> lstScoreEntity;

    public String getSumRoundNum() {
        return sumRoundNum;
    }

    public void setSumRoundNum(String sumRoundNum) {
        this.sumRoundNum = sumRoundNum;
    }

    public List<String> getLstDate() {
        return lstDate;
    }

    public void setLstDate(List<String> lstDate) {
        this.lstDate = lstDate;
    }

    public List<String> getLstDistance() {
        return lstDistance;
    }

    public void setLstDistance(List<String> lstDistance) {
        this.lstDistance = lstDistance;
    }

    public List<TblScoreEntity> getLstScoreEntity() {
        return lstScoreEntity;
    }

    public void setLstScoreEntity(List<TblScoreEntity> lstScoreEntity) {
        this.lstScoreEntity = lstScoreEntity;
    }

}
