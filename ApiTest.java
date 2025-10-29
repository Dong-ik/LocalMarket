import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class ApiTest {
    public static void main(String[] args) {
        System.out.println("=== LocalMarket 회원기능 DB 연동 테스트 ===\n");
        
        // 1. 데이터베이스 연결 테스트
        testApi("http://localhost:8080/api/test/db-connection", "데이터베이스 연결 테스트");
        
        // 2. 회원 목록 조회 테스트
        testApi("http://localhost:8080/api/test/members", "회원 목록 조회 테스트");
        
        // 3. ID 중복 체크 테스트
        testApi("http://localhost:8080/api/test/check-id/testuser", "ID 중복 체크 테스트 (testuser)");
        testApi("http://localhost:8080/api/test/check-id/admin", "ID 중복 체크 테스트 (admin)");
    }
    
    private static void testApi(String urlString, String testName) {
        try {
            System.out.println("🔍 " + testName);
            System.out.println("URL: " + urlString);
            
            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "application/json");
            
            int responseCode = conn.getResponseCode();
            System.out.println("응답 코드: " + responseCode);
            
            BufferedReader in = new BufferedReader(new InputStreamReader(
                responseCode == 200 ? conn.getInputStream() : conn.getErrorStream()));
            String inputLine;
            StringBuilder response = new StringBuilder();
            
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
            
            System.out.println("응답 내용:");
            System.out.println(response.toString());
            System.out.println("✅ " + (responseCode == 200 ? "성공" : "실패"));
            
        } catch (Exception e) {
            System.out.println("❌ 오류 발생: " + e.getMessage());
        }
        
        System.out.println("\n" + "=".repeat(60) + "\n");
    }
}