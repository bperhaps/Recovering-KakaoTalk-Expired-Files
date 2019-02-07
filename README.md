# Recovering KakaoTalk Expired Files
> īī���� �ӽ����� ���� ���α׷�

īī���� �ӽ������� �����մϴ�.

## �����
[**��α׿��� Ȯ��**](https://bperhaps.tistory.com/entry/%EC%B9%B4%EC%B9%B4%EC%98%A4%ED%86%A1-%EC%A0%80%EC%9E%A5%EA%B8%B0%EA%B0%84-%EB%A7%8C%EB%A3%8C-%ED%8C%8C%EC%9D%BC-%EB%B3%B5%EA%B5%AC-%ED%94%84%EB%A1%9C%EA%B7%B8%EB%9E%A8)

	* [**dcyang**](https://github.com/dcyang) ���� python-magic 5.19 ���� ���� ������ �̿��� python script �� ������Ʈ �� �ּ̽��ϴ�. [**[�ٷΰ���]**](https://gist.github.com/dcyang/7f857b5cc1a3c6d7f0ebc92a06dafd5d)
	
## ���� ����

īī�������� �������� �ְ�޴� ���� ����Ⱓ�� ����Ǽ� ������ �ٿ�ε� ���� ���ϴ� ��찡 �ִ�.
![](./img/1.jfif)
������ �� ����, �ٿ�ε�� �Ұ��������� �帴�ϰ� �ش� ������ �� �� �ִ°��� Ȯ���� �� �ִ�.
���⼭ �� ������ ������ �� ������ ���� �Ⱓ�� ���� �ٿ���� ���� ������ ���� �� ���� ������? �ϴ� �ǹ��� ǰ�� �Ǿ���.

���� īī������ �������� ��ġ�� �˾Ƴ��� �߰�, ��θ� ã�� ������.

īī���� Media���� ���� ��ġ:
```sh
/Android/data/com.kakao.talk
```
�ش� ��θ� ���� ������ ���� �������� �� �� �ִ�.
![](./img/2.png)
�츮�� ����� ������ contents �����̴�. ��ȭ�濡�� �ְ���� �������� �������� �� �����ȿ� �� ����ִٰ� ���� �ȴ�.
�ش� ������ ��ǻ�͵� �̵���Ų �� ���� ���빰�� ���� ???? �� ���� �� �ۿ� ����.
![](./img/3.png)
![](./img/4.png)
����3. Contents ������ ����

���ϰ� �������� �̸����� �ؽ�ȭ �Ǿ��ִ°��� �� �� ������ ������ ���ϵ��� Ȯ���ڵ� �������� �ʴ´�.
ó������ ���ϵ��� ��ȣȭ�� �ֳ�? �ϴ� ������ ����� �Ͽ����� ������ �װ��� �ƴϾ���.
![](./img/5.png)
�׸�3. HXD�� �̿��� Ȯ���� ���� ����

HXD�� �̿��� ���ϵ��� ����� ���� �ñ״�ó�� �ش� ������ ���������� ��Ȯ�ϰ� ����� �ְ� �־���.
�ʿ��� ������ ��� �����. �ڵ��� �ϸ� �ȴ�. ���̽��� �̿��Ͽ� �ڵ��� ���ε�, ���� �ñ״�ó�� �̿��� ���ϵ��� Ȯ���ڸ� �ٿ��� ���̴�.
������ ����, ������ ������ ���ϸ� ���� ���̶� �����Ͽ� ���̺귯���� �Ⱦ��� ���� �ñ״�ó���� ����Ʈ�� �־� ���� �����̾����� ������ ������(�� 1�� 6����)�� �м��� ���
![](./img/6.png)
����4. ���ϵ��� �� 4����Ʈ ����Ʈ

�����Ӹ� �ƴ϶� �ٸ� ����� ������ �ñ״�ó�� Ȯ�εǴ°� �� �� �־���.
����, ���� �������� �ñ״�ó�� ó���ϰ� �������� ����ó�����ѹ����� ���̺귯���� ���� �ʰ� ����� �����ϱ� ������.. �װ� ���ݻ� �ȸ±⵵ �ϰ� (����..) �׳� ���̺귯���� ����ϱ�� �ߴ�.
����� ���̺귯���� python-magic�̴�. 

```sh
# -*- coding: cp949 -*-
 
import sys
import os
import time
import shutil
import magic

 
# Referenced from "https://wikidocs.net/39"
def search(dirname):
    filenames = os.listdir(dirname)
    for filename in filenames:
        full_filename = os.path.join(dirname, filename)
        if os.path.isdir(full_filename):
            search(full_filename)
        else:
            path = time.strftime("%Y.%m.%d" , time.localtime(os.path.getmtime(full_filename)))
            extention = magic.from_file(full_filename, mime=True).split("/")[1]
            
            if not os.path.exists("./result/" + path):
                os.mkdir("./result/" + path)
 
            shutil.copy(full_filename, "./result/" + path + "/" + filename + "." + extention)     
 
if not os.path.exists("result"):
    os.mkdir("result");
 
search("/mnt/g/com.kakao.talk/contents")
```
�����ϰ� �ۼ��� �ڵ��̴�. ����� ����
![](./img/7.png)
![](./img/8.png)
����5. ���

��������� ��� ����� ������ ����, �ٿ�ε� �Ⱓ�� ���� �����̶� ������ �� �ִ� ���� �� �� �ִ�.

�� ������ ���Ͽ� �����ڵ带 �ۼ��� �� �־�����, �̰��� �������� java ���α׷����� �� ���� �Ͽ���.