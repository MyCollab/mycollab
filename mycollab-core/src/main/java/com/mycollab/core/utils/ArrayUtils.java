package com.mycollab.core.utils;

import com.mycollab.core.MyCollabException;
import org.apache.commons.beanutils.PropertyUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author MyCollab Ltd
 * @since 5.0.9
 */
public class ArrayUtils {
    public static <T> boolean isNotEmpty(T[] array) {
        return array != null && array.length != 0;
    }

    public static List<Integer> extractIds(List items) {
        try {
            List<Integer> keys = new ArrayList<>(items.size());
            for (Object item : items) {
                Integer key = (Integer) PropertyUtils.getProperty(item, "id");
                keys.add(key);
            }
            return keys;
        } catch (Exception e) {
            throw new MyCollabException(e);
        }
    }
}
