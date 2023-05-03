package com.capstone.giftWeb.Service;
import com.capstone.giftWeb.domain.Gift;
import com.capstone.giftWeb.dto.GiftsDto;
import com.capstone.giftWeb.dto.RecommendDto;
import org.apache.mahout.cf.taste.common.TasteException;
import org.apache.mahout.cf.taste.eval.RecommenderBuilder;
import org.apache.mahout.cf.taste.impl.common.FastByIDMap;
import org.apache.mahout.cf.taste.impl.model.jdbc.MySQLJDBCDataModel;
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

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Service
public class RecommendService {
    private RecommendDto recommendDto;

    private GiftsDto giftsDto;

    public void getRecommendDto(RecommendDto recommendDto) {
        this.recommendDto = recommendDto;
    }


    public List<Gift> recommend(Long userId) throws IOException, TasteException, SQLException {
        RandomUtils.useTestSeed(); // to randomize the evaluation result

        //DataModel model = new FileDataModel(new File("src/main/java/com/capstone/giftWeb/Service/dataset-recsys.csv"));

        MariaDbDataSource dataSource = new MariaDbDataSource();
        dataSource.setServerName("localhost");
        dataSource.setUser("root");
        dataSource.setPassword("byeonguk");
        dataSource.setDatabaseName("capstone");
        dataSource.setPortNumber(3307);
        JDBCDataModel model =
                new MySQLJDBCDataModel(dataSource, "gift_pref", "member_id", "gift_id", "pref_score", null);

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
                UserNeighborhood neighborhood = new NearestNUserNeighborhood(40, similarity, model);
                // user-based recommender 반환
                return new GenericUserBasedRecommender(model, neighborhood, similarity);
            }
        };

        // 여기서는 user-based recommender이 반환되어 recommender에 들어감
        Recommender recommender = recommenderBuilder.buildRecommender(model);
        // 유저 id 1에게 5개의 아이템을 추천해줌
        List<RecommendedItem> recommendations = recommender.recommend(userId, 20);
        List<Gift> recommendedGifts = new ArrayList<Gift>();

        // JDBC connection
        Connection conn = dataSource.getConnection();
        Statement stmt = conn.createStatement();

        for (RecommendedItem recommendedItem : recommendations) {

            String sql = "select * from gift where gift_id = " + recommendedItem.getItemID();
            ResultSet rs = stmt.executeQuery(sql);
            while(rs.next()) {
                Gift tmp = new Gift();
                tmp.setId(Long.valueOf(rs.getInt("gift_id")));
                tmp.setTitle(rs.getString("title"));
                tmp.setCompany(rs.getString("company"));
                tmp.setPrice(rs.getInt("price"));
                tmp.setCategory(rs.getInt("category"));
                tmp.setImage(rs.getString("image"));
                tmp.setHref(rs.getString("href"));
                recommendedGifts.add(tmp);
            }
        }

        return recommendedGifts;
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

}
