package com.epam.esm.gifts.controller;

import com.epam.esm.gifts.dto.TagDto;
import com.epam.esm.gifts.hateaos.HateoasBuilder;
import com.epam.esm.gifts.impl.StatisticsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/statistic/")
public class StatisticController {


    @Autowired
    StatisticsServiceImpl statisticsService;
    @Autowired
    HateoasBuilder hateoasBuilder;

    @GetMapping
    TagDto findTheMostPopularTag(){
        TagDto tagDto = statisticsService.mostWidelyUsedTag();
        hateoasBuilder.setLinks(tagDto);
        return tagDto;
    }
}
