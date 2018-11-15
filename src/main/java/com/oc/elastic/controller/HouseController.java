package com.oc.elastic.controller;

import com.oc.elastic.entity.House;
import com.oc.elastic.repository.HouseRepository;
import com.oc.elastic.util.BeanUtils;
import com.oc.elastic.vo.HouseVO;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

import static org.elasticsearch.index.query.QueryBuilders.matchAllQuery;
import static org.elasticsearch.index.query.QueryBuilders.matchQuery;

/**
 * @author SxL
 * Created on 11/15/2018 13:44.
 */
@RestController
public class HouseController {

    @Autowired
    private HouseRepository houseRepository;

    @Autowired
    private ElasticsearchTemplate elasticsearchTemplate;

    private List<House> houseList = new ArrayList<>();

    private List<HouseVO> houseVOList = new ArrayList<>();

    private static String INDEX = "demo";

    private static String TYPE = "doc";

    @GetMapping("/house")
    public List<HouseVO> listHouse() throws Exception {
        SearchQuery listHouse = new NativeSearchQueryBuilder()
                .withQuery(matchAllQuery())
                .withIndices(INDEX)
                .withTypes(TYPE)
                .withPageable(PageRequest.of(0, 10))
                .build();

        Page allHouse = houseRepository.search(listHouse);

        System.out.println(ToStringBuilder.reflectionToString(allHouse, ToStringStyle.JSON_STYLE));

        if (allHouse.getTotalPages() > 0) {
            houseList.addAll(allHouse.getContent());
            turnToVo();
        }

        return houseVOList;
    }

    @GetMapping("/house/{name}")
    public List<HouseVO> listHouseByName(@PathVariable("name") String name) {
        SearchQuery listHouseByName = new NativeSearchQueryBuilder()
                .withIndices(INDEX)
                .withTypes(TYPE)
                .withQuery(matchQuery("name", name))
                .withPageable(PageRequest.of(0, 10))
                .build();

        Page<House> housePage = houseRepository.search(listHouseByName);

        if (housePage.getTotalPages() >= 0) {
            houseList.addAll(housePage.getContent());
        }

        return houseVOList;
    }

    private void turnToVo() throws Exception {
        for (House house : houseList) {
            HouseVO houseVO = new HouseVO();
            BeanUtils.copyBeanToCamelNaming(house, houseVO);
            houseVOList.add(houseVO);
        }
    }
}
