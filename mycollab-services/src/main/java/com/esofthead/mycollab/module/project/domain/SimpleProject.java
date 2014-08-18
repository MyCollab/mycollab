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

	private String ownerName;

	private String accountName;

	private int numOpenBugs;

	private int numBugs;

	private int numOpenTasks;

	private int numTasks;

	private int numOpenProblems;

	private int numProblems;

	private int numOpenRisks;

	private int numRisks;

	private int numActiveMembers;

	private int numClosedPhase;

	private int numInProgressPhase;

	private int numFuturePhase;

	private double totalBillableHours;

	private double totalNonBillableHours;

	@NotBindable
	private Currency currency;

	public String getOwnerName() {
		return ownerName;
	}

	public void setOwnerName(String ownerName) {
		this.ownerName = ownerName;
	}

	public String getAccountName() {
		return accountName;
	}

	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}

	public int getNumOpenBugs() {
		return numOpenBugs;
	}

	public void setNumOpenBugs(int numOpenBugs) {
		this.numOpenBugs = numOpenBugs;
	}

	public int getNumBugs() {
		return numBugs;
	}

	public void setNumBugs(int numBugs) {
		this.numBugs = numBugs;
	}

	public int getNumOpenTasks() {
		return numOpenTasks;
	}

	public void setNumOpenTasks(int numOpenTasks) {
		this.numOpenTasks = numOpenTasks;
	}

	public int getNumTasks() {
		return numTasks;
	}

	public void setNumTasks(int numTasks) {
		this.numTasks = numTasks;
	}

	public int getNumOpenProblems() {
		return numOpenProblems;
	}

	public void setNumOpenProblems(int numOpenProblems) {
		this.numOpenProblems = numOpenProblems;
	}

	public int getNumProblems() {
		return numProblems;
	}

	public void setNumProblems(int numProblems) {
		this.numProblems = numProblems;
	}

	public int getNumOpenRisks() {
		return numOpenRisks;
	}

	public void setNumOpenRisks(int numOpenRisks) {
		this.numOpenRisks = numOpenRisks;
	}

	public int getNumRisks() {
		return numRisks;
	}

	public void setNumRisks(int numRisks) {
		this.numRisks = numRisks;
	}

	public Currency getCurrency() {
		return currency;
	}

	public void setCurrency(Currency currency) {
		this.currency = currency;
	}

	public int getNumActiveMembers() {
		return numActiveMembers;
	}

	public void setNumActiveMembers(int numActiveMembers) {
		this.numActiveMembers = numActiveMembers;
	}

	public int getNumClosedPhase() {
		return numClosedPhase;
	}

	public void setNumClosedPhase(int numClosedPhase) {
		this.numClosedPhase = numClosedPhase;
	}

	public int getNumInProgressPhase() {
		return numInProgressPhase;
	}

	public void setNumInProgressPhase(int numInProgressPhase) {
		this.numInProgressPhase = numInProgressPhase;
	}

	public int getNumFuturePhase() {
		return numFuturePhase;
	}

	public void setNumFuturePhase(int numFuturePhase) {
		this.numFuturePhase = numFuturePhase;
	}

	public double getTotalBillableHours() {
		return totalBillableHours;
	}

	public void setTotalBillableHours(double totalBillableHours) {
		this.totalBillableHours = totalBillableHours;
	}

	public double getTotalNonBillableHours() {
		return totalNonBillableHours;
	}

	public void setTotalNonBillableHours(double totalNonBillableHours) {
		this.totalNonBillableHours = totalNonBillableHours;
	}

	public boolean isProjectArchived() {
		return StatusI18nEnum.Archived.name().equals(this.getProjectstatus());
	}
}
