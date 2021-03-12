package org.DB;


import org.testng.annotations.Test;

import java.util.List;
import java.util.Map;


public class DbOperationTest {
    String url = "jdbc:mysql://172.16.200.87:3306/nemp";
    String userName = "root";
    String passWord = "CeShi#0301!";

    /**
     * 查询语句
     */
    @Test
    public void MySqlSelectTest() {

        String sql = "SELECT id, ad_name, platform_id, tags, is_global, is_true_name, module_code, show_num\n" +
                "FROM nemp.advertisement\n" +
                "WHERE id in(95,89);";
        DbOperation ms = new DbOperation();
        List<Map<String, Object>> ListResults = ms.MySqlSelect(url, userName, passWord,sql);
        for (int i = 0; i < ListResults.size(); i++) {
            Map<String, Object> map = ListResults.get(i);
            for (String key : map.keySet()) {
                System.out.print(key + "=" + map.get(key) + "   ");
            }
            System.out.println();
            System.out.println(map.get("ad_name").toString());
            //Assert.assertEquals("吞吞吐吐 ", map.get("ad_name").toString());

        }
    }
//

    @Test
    public void MysqlInsertTest() {
        String sql = "INSERT INTO nemp.advertisement " +
                "(ad_name, platform_id, img_url, h5_url, type, start_time, end_time, create_time, update_time, turn_time, state, tags, is_global, is_true_name, module_code, show_num)VALUES('广告名2字', 120, '', '', 2, '2020-10-14 16:31:18', '2021-01-26 16:31:18', '2020-10-14 16:31:09', '2020-10-14 16:31:09', 05, '0', '0', 1, 0, '', 0);";

        DbOperation dboperation = new DbOperation();
        dboperation.MysqlInsert(url, userName, passWord,sql);
    }

    @Test
    public void MysqlDeleteTest() {
        String sql = "DELETE FROM nemp.advertisement\n" +
                "WHERE platform_id=120;";
        DbOperation dboperation = new DbOperation();
        dboperation.MysqlDelete(url, userName, passWord,sql);
    }

    @Test
    public void MysqlUpdateTest() {
        String sql = "UPDATE nemp.advertisement\n" +
                "SET ad_name='十六点d就发了' WHERE platform_id=120;";
        DbOperation dboperation = new DbOperation();
        dboperation.MysqlUpdate(url, userName, passWord,sql);
    }

}
