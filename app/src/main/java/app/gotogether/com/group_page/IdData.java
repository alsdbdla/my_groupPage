package app.gotogether.com.group_page;

/**
 * Created by user on 2017-09-13.
 */

public class IdData {

    // 이렇게 값을 담는 클래스를 VO 또는 DTO 라고 부른다.
    // 원본 데이터를 담음
    // 여기까지가 레이아웃을 만들고 원본 데이터를 담는 곳을 만드는 과정 그 다음 어댑터 만들어야함

    private String my_id;
    private String group_id;
    //alt + insert : 게터 세터 생성, 생성자 생성


    public IdData(String my_id, String group_id) {
        this.my_id = my_id;
        this.group_id = group_id;
    }

    public String get_my_id() {
        return my_id;
    }

    public void set_my_id(String my_id) {
        this.my_id = my_id;
    }

    public String get_group_id() {
        return group_id;
    }

    public void set_group_id(String group_id) {
        this.group_id = group_id;
    }

}
