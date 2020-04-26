import dao.SampleDAO;
import junit.framework.TestCase;
import org.junit.Test;

public class TestJunit extends TestCase {

    String uploaded_by = "Jeff";
    SampleDAO sampleDAO = new SampleDAO();

    @Test
    public void testPrintMessage() {
        assertEquals(uploaded_by, sampleDAO.findById(1).getUploadedBy());
    }

}
