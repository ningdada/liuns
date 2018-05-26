package io.stream;


import com.alibaba.fastjson.JSON;
import model.business.po.UserPO;

import java.io.*;
import java.util.Date;

/**
 * ObjectInputStream   ObjectOutputStream
 * <p />
 * 适合用来处理字节流
 */
public class ObjectIOStreamDemo {

    public static void main(String[] args) throws IOException, ClassNotFoundException {

        UserPO user = new UserPO();
        user.setId(123L);
        user.setUsername("宁大大");
        user.setPwd("pwd,2!");
        user.setCreateTime(new Date());

        // 将user对象序列化到文件中
        FileOutputStream out = new FileOutputStream("/Users/ningdd/Downloads/operate.txt");
        ObjectOutputStream objectOut = new ObjectOutputStream(out);
        objectOut.writeObject(user);

        out.close();
        objectOut.close();

        // 从文件中反序列化得到user对象
        FileInputStream in = new FileInputStream("/Users/ningdd/Downloads/operate.txt");
        ObjectInputStream objectIn = new ObjectInputStream(in);
        UserPO newUser = (UserPO) objectIn.readObject();
        System.out.println(JSON.toJSONString(newUser));
    }
}
