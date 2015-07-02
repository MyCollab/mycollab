/**
 * This file is part of mycollab-services.
 *
 * mycollab-services is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * mycollab-services is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with mycollab-services.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.esofthead.mycollab.module.user.dao;

import com.esofthead.mycollab.core.persistence.ICrudGenericDAO;
import com.esofthead.mycollab.module.user.domain.UserTracking;
import com.esofthead.mycollab.module.user.domain.UserTrackingExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

@SuppressWarnings({ "ucd", "rawtypes" })
public interface UserTrackingMapper extends ICrudGenericDAO {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table s_user_tracking
     *
     * @mbggenerated Wed Jul 01 17:31:40 ICT 2015
     */
    int countByExample(UserTrackingExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table s_user_tracking
     *
     * @mbggenerated Wed Jul 01 17:31:40 ICT 2015
     */
    int deleteByExample(UserTrackingExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table s_user_tracking
     *
     * @mbggenerated Wed Jul 01 17:31:40 ICT 2015
     */
    int deleteByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table s_user_tracking
     *
     * @mbggenerated Wed Jul 01 17:31:40 ICT 2015
     */
    int insert(UserTracking record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table s_user_tracking
     *
     * @mbggenerated Wed Jul 01 17:31:40 ICT 2015
     */
    int insertSelective(UserTracking record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table s_user_tracking
     *
     * @mbggenerated Wed Jul 01 17:31:40 ICT 2015
     */
    List<UserTracking> selectByExampleWithBLOBs(UserTrackingExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table s_user_tracking
     *
     * @mbggenerated Wed Jul 01 17:31:40 ICT 2015
     */
    List<UserTracking> selectByExample(UserTrackingExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table s_user_tracking
     *
     * @mbggenerated Wed Jul 01 17:31:40 ICT 2015
     */
    UserTracking selectByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table s_user_tracking
     *
     * @mbggenerated Wed Jul 01 17:31:40 ICT 2015
     */
    int updateByExampleSelective(@Param("record") UserTracking record, @Param("example") UserTrackingExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table s_user_tracking
     *
     * @mbggenerated Wed Jul 01 17:31:40 ICT 2015
     */
    int updateByExampleWithBLOBs(@Param("record") UserTracking record, @Param("example") UserTrackingExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table s_user_tracking
     *
     * @mbggenerated Wed Jul 01 17:31:40 ICT 2015
     */
    int updateByExample(@Param("record") UserTracking record, @Param("example") UserTrackingExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table s_user_tracking
     *
     * @mbggenerated Wed Jul 01 17:31:40 ICT 2015
     */
    int updateByPrimaryKeySelective(UserTracking record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table s_user_tracking
     *
     * @mbggenerated Wed Jul 01 17:31:40 ICT 2015
     */
    int updateByPrimaryKeyWithBLOBs(UserTracking record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table s_user_tracking
     *
     * @mbggenerated Wed Jul 01 17:31:40 ICT 2015
     */
    int updateByPrimaryKey(UserTracking record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table s_user_tracking
     *
     * @mbggenerated Wed Jul 01 17:31:40 ICT 2015
     */
    Integer insertAndReturnKey(UserTracking value);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table s_user_tracking
     *
     * @mbggenerated Wed Jul 01 17:31:40 ICT 2015
     */
    void removeKeysWithSession(List primaryKeys);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table s_user_tracking
     *
     * @mbggenerated Wed Jul 01 17:31:40 ICT 2015
     */
    void massUpdateWithSession(@Param("record") UserTracking record, @Param("primaryKeys") List primaryKeys);
}