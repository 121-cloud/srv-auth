package guice;

/**
 * zhangyef@yonyou.com on 2015-10-28.
 */
public class OtherFun {
    private int status;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public void func(){
        System.out.println("In other fun : " + status);
    }
}
