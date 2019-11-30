package com.xuecheng.manage_cms.dao;

import com.xuecheng.framework.domain.cms.CmsPage;
import org.csource.common.MyException;
import org.csource.fastdfs.ClientGlobal;
import org.csource.fastdfs.StorageClient;
import org.csource.fastdfs.TrackerClient;
import org.csource.fastdfs.TrackerServer;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.util.*;

//@SpringBootTest
//@RunWith(value = SpringRunner.class)
public class TestCms {

    @Autowired
    private CmsPageDao cmsPageDao;

    @Test
    public void test() {
        //暂时采用测试数据，测试接口是否可以正常运行
        Optional<CmsPage> byId = cmsPageDao.findById("5a754adf6abb500ad05688d9");
        if (byId.isPresent()) {
            CmsPage cmsPage = byId.get();
            System.out.println(cmsPage.getPageName());
        }else {
            CmsPage cmsPage = null;
        }

    }

    @Test
    public void test1() throws IOException, MyException {

        ClientGlobal.initByProperties("fastDFS.properties");
        TrackerClient trackerClient = new TrackerClient();
        TrackerServer connection = trackerClient.getConnection();
        StorageClient storageClient = new StorageClient(connection, null);

        String fileName = "F://我的图片//20180802103048109.jpg";
        String[] jpgs = storageClient.upload_file(fileName, "jpg", null);
        for (int i = 0; i < jpgs.length; i++) {
            String s = jpgs[i];
            System.out.println("s="+s);
        }

        //M00/00/00/wKglgl1xCHqAQnQLABAGykl6DsM076.jpg

//        int group1 = storageClient.delete_file("group1",
//                "M00/00/00/wKglgl1xCHqAQnQLABAGykl6DsM076.jpg");
//        System.out.println(group1);
    }

    @Test
    public void test2() {
        HashMap<Integer, User> users = new HashMap<Integer, User>();
        users.put(1, new User("张三", 25));
        users.put(3, new User("李四", 22));
        users.put(2, new User("王五", 28));

        users = sortHashMap(users);
    }

    private HashMap<Integer, User> sortHashMap(HashMap<Integer, User> hashMap) {
        Set<Map.Entry<Integer, User>> entries = hashMap.entrySet();
        List<Map.Entry<Integer, User>> list = new ArrayList<>(entries);
        Collections.sort(list, new Comparator<Map.Entry<Integer, User>>() {
            @Override
            public int compare(Map.Entry<Integer, User> o1, Map.Entry<Integer, User> o2) {
                return o2.getValue().getAge()-o1.getValue().getAge();
            }
        });
        LinkedHashMap<Integer, User> linkedHashMap = new LinkedHashMap<>();
        for (Map.Entry<Integer, User> entry : list) {
            linkedHashMap.put(entry.getKey(), entry.getValue());
        }
        return linkedHashMap;
    }
    class User {
        private String name;
        private Integer age;

        public User(String name, Integer age) {
            this.name = name;
            this.age = age;
        }
        public User() {}
        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Integer getAge() {
            return age;
        }

        public void setAge(Integer age) {
            this.age = age;
        }
    }

}
