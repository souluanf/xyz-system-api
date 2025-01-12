package dev.luanfernandes.domain.constants;

import lombok.experimental.UtilityClass;

@UtilityClass
public class PathConstants {
    private static final String API_V1 = "/v1";

    public static final String AUTH_V1 = API_V1 + "/auth";
    public static final String AUTH_TOKEN = AUTH_V1 + "/token";

    public static final String ANAGRAMS_V1 = API_V1 + "/anagrams";

    public static final String PLANTS_V1 = API_V1 + "/plants";
    public static final String PLANTS_V1_ID = PLANTS_V1 + "/{id}";

    public static final String USER_V1 = API_V1 + "/users";
    public static final String USER_V1_ID = USER_V1 + "/{username}";

    public static final String SALES_PERSON_V1 = API_V1 + "/sales-persons";
    public static final String SALES_PERSON_V1_WITHOUT_ORDERS = SALES_PERSON_V1 + "/without-orders";
    public static final String SALES_PERSON_V1_MORE_THAN_ONE_ORDER = SALES_PERSON_V1 + "/more-than-one-order";
    public static final String SALES_PERSON_V1_TOTAL_SALES = SALES_PERSON_V1 + "/total-sales";
    public static final String SALES_PERSON_V1_UPDATE_NAMES = SALES_PERSON_V1 + "/update-names";
    public static final String SALES_PERSON_V1_DELETE_BY_CITY = SALES_PERSON_V1 + "/delete-by-city";
}
