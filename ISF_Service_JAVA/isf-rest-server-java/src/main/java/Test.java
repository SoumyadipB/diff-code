
public class Test {

	public void T1(String s1) {
		System.out.println("String S1 is "+s1);
	}
	public void T1(Object obj) {
		System.out.println("String Object is "+obj);
	}
	public static void main(String[] args) {
		Test t=new Test();
t.T1(null);
	}

}
