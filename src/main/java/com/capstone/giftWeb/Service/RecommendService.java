package com.capstone.giftWeb.Service;
import com.capstone.giftWeb.dto.RecommendDto;
import org.apache.mahout.cf.taste.common.Refreshable;
import org.apache.mahout.cf.taste.common.TasteException;
import org.apache.mahout.cf.taste.eval.RecommenderBuilder;
import org.apache.mahout.cf.taste.impl.common.FastByIDMap;
import org.apache.mahout.cf.taste.impl.common.FastIDSet;
import org.apache.mahout.cf.taste.impl.common.LongPrimitiveIterator;
import org.apache.mahout.cf.taste.impl.common.jdbc.AbstractJDBCComponent;
import org.apache.mahout.cf.taste.impl.model.AbstractDataModel;
import org.apache.mahout.cf.taste.impl.model.GenericDataModel;
import org.apache.mahout.cf.taste.impl.model.GenericItemPreferenceArray;
import org.apache.mahout.cf.taste.impl.model.file.FileDataModel;
import org.apache.mahout.cf.taste.impl.neighborhood.NearestNUserNeighborhood;
import org.apache.mahout.cf.taste.impl.recommender.GenericUserBasedRecommender;
import org.apache.mahout.cf.taste.impl.similarity.EuclideanDistanceSimilarity;
import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.cf.taste.model.JDBCDataModel;
import org.apache.mahout.cf.taste.model.PreferenceArray;
import org.apache.mahout.cf.taste.neighborhood.UserNeighborhood;
import org.apache.mahout.cf.taste.recommender.RecommendedItem;
import org.apache.mahout.cf.taste.recommender.Recommender;
import org.apache.mahout.cf.taste.similarity.UserSimilarity;
import org.apache.mahout.common.RandomUtils;
import org.mariadb.jdbc.MariaDbDataSource;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.io.File;
import java.io.IOException;
import java.sql.*;
import java.util.Collection;
import java.util.List;

@Service
public class RecommendService {
    private RecommendDto recommendDto;

    public void getRecommendDto(RecommendDto recommendDto) {
        this.recommendDto = recommendDto;
    }

    public List<RecommendedItem> recommend(int id) throws IOException, TasteException, SQLException {
        RandomUtils.useTestSeed(); // to randomize the evaluation result

        DataModel model = new FileDataModel(new File("src/main/java/com/capstone/giftWeb/Service/dataset-recsys.csv"));

        final String driver = "org.mariadb.jdbc.Driver";
        final String DB_IP = "localhost";
        final String DB_PORT = "3306";
        final String DB_NAME = "capstone";
        final String DB_URL = "jdbc:mariadb://" + DB_IP + ":" + DB_PORT + "/" + DB_NAME;

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            Class.forName(driver);
            conn = DriverManager.getConnection(DB_URL, "root", "byeonguk");
            if (conn != null) {
                System.out.println("DB 접속 성공");
            }

        } catch (ClassNotFoundException e) {
            System.out.println("드라이버 로드 실패");
            e.printStackTrace();
        } catch (SQLException e) {
            System.out.println("DB 접속 실패");
            e.printStackTrace();
        }

        try {
            String sql = "select * from ";

            pstmt = conn.prepareStatement(sql);

            rs = pstmt.executeQuery();
            String userId = null;
            Long categoryId = null;
            Long prefScore = null;
            while (rs.next()) {
                userId = rs.getString(1);
                categoryId = Long.parseLong(rs.getString(2));
                prefScore = Long.parseLong(rs.getString(3));
            }

            System.out.println(userId);
            System.out.println(categoryId);
            System.out.println(prefScore);

        } catch (SQLException e) {
            System.out.println("error: " + e);
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (pstmt != null) {
                    pstmt.close();
                }

                if (conn != null && !conn.isClosed()) {
                    conn.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        long key = 0;
        FastByIDMap<PreferenceArray> preferences = new FastByIDMap<PreferenceArray>();


        RecommenderBuilder recommenderBuilder = new RecommenderBuilder() {
            public Recommender buildRecommender(DataModel model) throws TasteException {
                // 유클리드 거리로 유사도 측정
                // 유사도 측정법엔 1) 유클리디안 거리 2) 마할라노비스 거리 3) 맨하탄 거리 4) 민코스키 거리 5) 코사인 유사도 6) 피어슨 상관계수
                // 상기 6가지 유사도 모두 mahout에서 사용 가능
                UserSimilarity similarity = new EuclideanDistanceSimilarity(model);
                //UserSimilarity similarity = new PearsonCorrelationSimilarity(model);
                //SpearmanCorrelationSimilarity similarity = new SpearmanCorrelationSimilarity(model);

                // nn보다 잠재적 요인 방법이 효율적으로 보이나, 향후 테스트하고 우선적으로 nn 사용
                // neighborhood size = 100
                UserNeighborhood neighborhood = new NearestNUserNeighborhood(100, similarity, model);
                // user-based recommender 반환
                return new GenericUserBasedRecommender(model, neighborhood, similarity);
            }
        };

        // 여기서는 user-based recommender이 반환되어 recommender에 들어감
        Recommender recommender = recommenderBuilder.buildRecommender(model);
        // 유저 id 1에게 5개의 아이템을 추천해줌
        List<RecommendedItem> recommendations = recommender.recommend(1, 5);

        for (RecommendedItem recommendedItem : recommendations) {
            System.out.println(recommendedItem);
        }

        return recommendations;
    }
    /*
    public static void main(String[] args) throws IOException, TasteException {

        RandomUtils.useTestSeed(); // to randomize the evaluation result
        DataModel model = new FileDataModel(new File("src/main/java/com/capstone/giftWeb/Service/dataset-recsys.csv"));

        RecommenderBuilder recommenderBuilder = new RecommenderBuilder() {
            public Recommender buildRecommender(DataModel model) throws TasteException {

                // 유클리드 거리로 유사도 측정
                // 유사도 측정법엔 1) 유클리디안 거리 2) 마할라노비스 거리 3) 맨하탄 거리 4) 민코스키 거리 5) 코사인 유사도 6) 피어슨 상관계수
                // 상기 6가지 유사도 모두 mahout에서 사용 가능
                UserSimilarity similarity = new EuclideanDistanceSimilarity(model);
                //UserSimilarity similarity = new PearsonCorrelationSimilarity(model);
                //SpearmanCorrelationSimilarity similarity = new SpearmanCorrelationSimilarity(model);

                // nn보다 잠재적 요인 방법이 효율적으로 보이나, 향후 테스트하고 우선적으로 nn 사용
                // neighborhood size = 100
                UserNeighborhood neighborhood = new NearestNUserNeighborhood(100, similarity, model);
                // user-based recommender 반환
                return new GenericUserBasedRecommender(model, neighborhood, similarity);
            }
        };

        // 여기서는 user-based recommender이 반환되어 recommender에 들어감
        Recommender recommender = recommenderBuilder.buildRecommender(model);
        // 유저 id 1에게 5개의 아이템을 추천해줌
        List<RecommendedItem> recommendations = recommender.recommend(1, 5);

        for (RecommendedItem recommendedItem : recommendations) {
            System.out.println(recommendedItem);
        }

        //return recommendations;

        /*
        RecommenderEvaluator evaluator = new RMSRecommenderEvaluator();
        double score = evaluator.evaluate(recommenderBuilder, null, model, 0.7, 1.0);
        System.out.println("RMSE: " + score);

        RecommenderIRStatsEvaluator statsEvaluator = new GenericRecommenderIRStatsEvaluator();
        IRStatistics stats = statsEvaluator.evaluate(recommenderBuilder, null, model, null, 10, 4, 0.7); // evaluate precision recall at 10

        System.out.println("Precision: " + stats.getPrecision());
        System.out.println("Recall: " + stats.getRecall());
        System.out.println("F1 Score: " + stats.getF1Measure());

    }
    */

    /*
    public List<RecommendedItem> RecommendItem(File file) throws Exception {

    }
    */
}
