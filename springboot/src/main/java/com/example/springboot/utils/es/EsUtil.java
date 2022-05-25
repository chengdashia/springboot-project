//package com.example.springboot.utils.es;
//
//
//import com.nongXingGang.pojo.es.DemandEs;
//import com.nongXingGang.pojo.es.GoodEs;
//import org.apache.commons.lang3.StringUtils;
//import org.elasticsearch.action.search.SearchResponse;
//import org.elasticsearch.search.suggest.Suggest;
//import org.elasticsearch.search.suggest.SuggestBuilder;
//import org.elasticsearch.search.suggest.SuggestBuilders;
//import org.elasticsearch.search.suggest.completion.CompletionSuggestionBuilder;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
//import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
//import org.springframework.stereotype.Component;
//
//import java.util.HashSet;
//import java.util.List;
//import java.util.Set;
//import java.util.stream.Collectors;
//
///**
// * @author 成大事
// * @since 2022/5/22 11:52
// */
//@Component
//public class EsUtil {
//
//    @Autowired
//    private ElasticsearchOperations elasticsearchOperations;
//
//    @Autowired
//    private ElasticsearchRestTemplate restTemplate;
//
//    /**
//     * 商品 后缀匹配
//     * @param keyWord 关键词  条数
//     * @param count  条数
//     * @return list
//     */
//    public List<String> getGoodsMatchList(String keyWord, int count){
//        CompletionSuggestionBuilder suggestion = SuggestBuilders
//                .completionSuggestion("suggest").prefix(keyWord).size(count).skipDuplicates(true);
//        SuggestBuilder suggestBuilder = new SuggestBuilder();
//        suggestBuilder.addSuggestion("doc", suggestion);
//        //ESBlog1.class用于确定是哪一个index，ESBlog1是一个实体类
//        SearchResponse response = restTemplate.suggest(suggestBuilder, GoodEs.class);
//        Suggest suggest = response.getSuggest();
//
//        Set<String> keywords = null;
//        if (suggest != null) {
//            keywords = new HashSet<>();
//            List<? extends Suggest.Suggestion.Entry<? extends Suggest.Suggestion.Entry.Option>> entries
//                    = suggest.getSuggestion("doc").getEntries();
//
//            for (Suggest.Suggestion.Entry<? extends Suggest.Suggestion.Entry.Option> entry : entries) {
//                for (Suggest.Suggestion.Entry.Option option: entry.getOptions()) {
//                    // 最多返回9个数据, 每个长度最大为20
//                    String keyword = option.getText().string();
//                    if (!StringUtils.isEmpty(keyword) && keyword.length() <= 20) {
//
//                        keywords.add(keyword);
//                        if (keywords.size() >= 9) {
//                            break;
//                        }
//                    }
//                }
//            }
//        }
//        List<String> collect = keywords.stream().collect(Collectors.toList());
//        return collect;
//    }
//
//
//    /**
//     * 需求 后缀匹配
//     * @param keyWord 关键词  条数
//     * @param count  条数
//     * @return list
//     */
//    public List<String> getDemandMatchList(String keyWord, int count){
////        List<String> list = new ArrayList<>();
//////        String keyword = "苹";
////        // 使用suggest进行标题联想
////        CompletionSuggestionBuilder suggest = SuggestBuilders.completionSuggestion("suggest")
////                // 关键字（参数传此）
////                .prefix(keyWord)
////                // 重复过滤
////                .skipDuplicates(true)
////                // 匹配数量
////                .size(count);
////        SuggestBuilder suggestBuilder = new SuggestBuilder();
////        suggestBuilder.addSuggestion("my-suggest",suggest);
////
////        IndexCoordinates indexCoordinates = elasticsearchOperations.getIndexCoordinatesFor(DemandEs.class);
////
////        // 查询
////        SearchResponse goodsNameSuggestResp = restTemplate.suggest(suggestBuilder, indexCoordinates);
////        Suggest.Suggestion<? extends Suggest.Suggestion.Entry<? extends Suggest.Suggestion.Entry.Option>> goodsNameSuggest = goodsNameSuggestResp
////                .getSuggest().getSuggestion("my-suggest");
////
////        // 处理返回
////        List<String> suggests = goodsNameSuggest.getEntries().stream().map(x -> x.getOptions().stream().map(y->y.getText().toString()).collect(Collectors.toList())).findFirst().get();
////        // 输出内容
////        for (String s : suggests) {
////            System.out.println("suggest = " + s);
////            list.add(s);
////        }
////        return list;
//
//        CompletionSuggestionBuilder suggestion = SuggestBuilders
//                .completionSuggestion("suggest").prefix(keyWord).size(count).skipDuplicates(true);
//        SuggestBuilder suggestBuilder = new SuggestBuilder();
//        suggestBuilder.addSuggestion("doc", suggestion);
//        //ESBlog1.class用于确定是哪一个index，ESBlog1是一个实体类
//        SearchResponse response = restTemplate.suggest(suggestBuilder, DemandEs.class);
//        Suggest suggest = response.getSuggest();
//
//        Set<String> keywords = null;
//        if (suggest != null) {
//            keywords = new HashSet<>();
//            List<? extends Suggest.Suggestion.Entry<? extends Suggest.Suggestion.Entry.Option>> entries
//                    = suggest.getSuggestion("doc").getEntries();
//
//            for (Suggest.Suggestion.Entry<? extends Suggest.Suggestion.Entry.Option> entry : entries) {
//                for (Suggest.Suggestion.Entry.Option option: entry.getOptions()) {
//                    // 最多返回9个数据, 每个长度最大为20
//                    String keyword = option.getText().string();
//                    if (!StringUtils.isEmpty(keyword) && keyword.length() <= 20) {
//
//                        keywords.add(keyword);
//                        if (keywords.size() >= 9) {
//                            break;
//                        }
//                    }
//                }
//            }
//        }
//        List<String> collect = keywords.stream().collect(Collectors.toList());
//        return collect;
//    }
//
//
//
//}
