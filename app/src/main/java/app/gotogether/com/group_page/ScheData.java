package app.gotogether.com.group_page;

/**
 * Created by user on 2017-09-10.
 */

public class ScheData {

    // 이렇게 값을 담는 클래스를 VO 또는 DTO 라고 부른다.
    // 원본 데이터를 담음
    // 여기까지가 레이아웃을 만들고 원본 데이터를 담는 곳을 만드는 과정 그 다음 어댑터 만들어야함

    private String plan;
    private String _id;
    //alt + insert : 게터 세터 생성, 생성자 생성


    public ScheData(String _id, String plan) {
        this.plan = plan;
        this._id = _id;
    }

    public String getPlan() {
        return plan;
    }

    public void setPlan(String plan) {
        this.plan = plan;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

}
