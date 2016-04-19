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
package com.esofthead.mycollab.module.project.domain;

import com.esofthead.mycollab.common.domain.Currency;
import com.esofthead.mycollab.common.i18n.OptionI18nEnum.StatusI18nEnum;
import com.esofthead.mycollab.core.arguments.NotBindable;

public class SimpleProject extends Project {
    private static final long serialVersionUID = 1L;

    private String createUserFullName;

    private Integer numOpenBugs;

    private Integer numBugs;

    private Integer numOpenTasks;

    private Integer numTasks;

    private Integer numOpenRisks;

    private Integer numRisks;

    private Integer numActiveMembers;

    private Integer numClosedPhase;

    private Integer numInProgressPhase;

    private Integer numFuturePhase;

    private Double totalBillableHours;

    private Double totalNonBillableHours;

    private String leadFullName;

    private String leadAvatarId;

    private String clientName;

    private String clientAvatarId;

    @NotBindable
    private ProjectCustomizeView customizeView;

    @NotBindable
    private Currency currency;

    public String getCreateUserFullName() {
        return createUserFullName;
    }

    public void setCreateUserFullName(String createUserFullName) {
        this.createUserFullName = createUserFullName;
    }

    public Integer getNumOpenBugs() {
        return numOpenBugs;
    }

    public void setNumOpenBugs(Integer numOpenBugs) {
        this.numOpenBugs = numOpenBugs;
    }

    public Integer getNumBugs() {
        return numBugs;
    }

    public void setNumBugs(Integer numBugs) {
        this.numBugs = numBugs;
    }

    public Integer getNumOpenTasks() {
        return numOpenTasks;
    }

    public void setNumOpenTasks(Integer numOpenTasks) {
        this.numOpenTasks = numOpenTasks;
    }

    public Integer getNumTasks() {
        return numTasks;
    }

    public void setNumTasks(Integer numTasks) {
        this.numTasks = numTasks;
    }

    public Integer getNumOpenRisks() {
        return numOpenRisks;
    }

    public void setNumOpenRisks(Integer numOpenRisks) {
        this.numOpenRisks = numOpenRisks;
    }

    public Integer getNumRisks() {
        return numRisks;
    }

    public void setNumRisks(Integer numRisks) {
        this.numRisks = numRisks;
    }

    public Integer getNumActiveMembers() {
        return numActiveMembers;
    }

    public void setNumActiveMembers(Integer numActiveMembers) {
        this.numActiveMembers = numActiveMembers;
    }

    public Integer getNumClosedPhase() {
        return numClosedPhase;
    }

    public void setNumClosedPhase(Integer numClosedPhase) {
        this.numClosedPhase = numClosedPhase;
    }

    public Integer getNumInProgressPhase() {
        return numInProgressPhase;
    }

    public void setNumInProgressPhase(Integer numInProgressPhase) {
        this.numInProgressPhase = numInProgressPhase;
    }

    public Integer getNumFuturePhase() {
        return numFuturePhase;
    }

    public void setNumFuturePhase(Integer numFuturePhase) {
        this.numFuturePhase = numFuturePhase;
    }

    public Double getTotalBillableHours() {
        return totalBillableHours;
    }

    public void setTotalBillableHours(Double totalBillableHours) {
        this.totalBillableHours = totalBillableHours;
    }

    public Double getTotalNonBillableHours() {
        return totalNonBillableHours;
    }

    public void setTotalNonBillableHours(Double totalNonBillableHours) {
        this.totalNonBillableHours = totalNonBillableHours;
    }

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    public boolean isProjectArchived() {
        return StatusI18nEnum.Archived.name().equals(this.getProjectstatus());
    }

    public ProjectCustomizeView getCustomizeView() {
        return customizeView;
    }

    public void setCustomizeView(ProjectCustomizeView customizeView) {
        this.customizeView = customizeView;
    }

    public boolean isArchived() {
        return StatusI18nEnum.Archived.name().equals(getProjectstatus());
    }

    public String getLeadFullName() {
        return leadFullName;
    }

    public void setLeadFullName(String leadFullName) {
        this.leadFullName = leadFullName;
    }

    public String getLeadAvatarId() {
        return leadAvatarId;
    }

    public void setLeadAvatarId(String leadAvatarId) {
        this.leadAvatarId = leadAvatarId;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public String getClientAvatarId() {
        return clientAvatarId;
    }

    public void setClientAvatarId(String clientAvatarId) {
        this.clientAvatarId = clientAvatarId;
    }

    public enum Field {
        leadFullName, totalBillableHours, totalNonBillableHours, clientName;

        public boolean equalTo(Object value) {
            return name().equals(value);
        }
    }
}
