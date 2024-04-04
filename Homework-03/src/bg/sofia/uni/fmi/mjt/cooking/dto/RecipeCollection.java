package bg.sofia.uni.fmi.mjt.cooking.dto;

import bg.sofia.uni.fmi.mjt.cooking.dto.nextpage.NextPage;

import java.util.List;

public record RecipeCollection(int count, NextPage _links, List<RecipeContainer> hits) {
}
