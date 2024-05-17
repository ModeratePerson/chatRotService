import com.moderatePerson.Application;
import com.moderatePerson.domain.PO.User;
import com.moderatePerson.mapper.UserMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SpringBootTest(classes = Application.class)
@RunWith(SpringJUnit4ClassRunner.class)
public class LoginTest {
    @Autowired
    public UserMapper userMapper;
    @Test
    public void loginTest(){
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("username", "user1");
        queryParams.put("password", "password1");
        List<User> map = userMapper.selectByMap(queryParams);
        boolean auth = false;
        if (map != null){
            auth = true;
        }
        System.out.println(auth);
    }
}
