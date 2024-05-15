import com.aliyuncs.exceptions.ClientException;
import com.moderatePerson.utils.sms.SmsService;
import org.junit.Test;

public class SmsTest {
    @Test
    public void smsTest() throws ClientException {
        SmsService.sendCode("电话号","234874");
    }
}
