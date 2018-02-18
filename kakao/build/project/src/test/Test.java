package test;

public class Test {
	private String name = "bperhaps";

	public void setName(String name){
		this.name = name;
	}

	public String getName(){
		return name;
	}

	public void doSomething(Test t){
		System.out.println(t.getName());
	}

	public static void main(String args[]){
		Test t = new Test();
		t.doSomething(new Test(){
			public String name = "cperhaps";

			@Override
			public String getName(){
				return name;
			}
		});
	}
}
