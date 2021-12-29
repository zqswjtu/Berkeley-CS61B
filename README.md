# Berkeley-CS61B
Berkeley-CS61B Data Structures Spring 2021

##### 设置 git 用户名和邮箱 #####

```shell
 $ git config --global user.email "you@berkeley.edu"
 $ git config --global user.name "Your Name"
```

##### github 创建远程仓库，并克隆到本地

```shell
$ git clone https://github.com/zqswjtu/Berkeley-CS61B.git
```

##### 进入项目文件夹（我本地是Berkeley-CS61B），添加远程分支并拉取任务代码

```shell
 $ git remote add skeleton https://github.com/Berkeley-CS61B/skeleton-sp21.git
 $ git pull --rebase --allow-unrelated-histories skeleton master
```

##### 操作成功后可以拉取到本课程需要完成的相关代码项目

![image-20211229160943215](C:\Users\Gerald\AppData\Roaming\Typora\typora-user-images\image-20211229160943215.png)

##### 拉取完毕后使用 git status 查看项目状态，提示使用 git pull 并入远程分支

![image-20211229160803739](C:\Users\Gerald\AppData\Roaming\Typora\typora-user-images\image-20211229160803739.png)

```shell
$ git pull --allow-unrelated-histories
```

##### 操作成功后将本地的代码推送至远程分支

```shell
$ git push origin master
```

