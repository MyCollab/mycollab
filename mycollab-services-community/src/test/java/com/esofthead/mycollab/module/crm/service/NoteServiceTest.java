/**
 * This file is part of mycollab-services-community.
 *
 * mycollab-services-community is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * mycollab-services-community is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with mycollab-services-community.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.esofthead.mycollab.module.crm.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.esofthead.mycollab.core.arguments.NumberSearchField;
import com.esofthead.mycollab.core.arguments.SearchField;
import com.esofthead.mycollab.core.arguments.SearchRequest;
import com.esofthead.mycollab.module.crm.domain.Note;
import com.esofthead.mycollab.module.crm.domain.SimpleNote;
import com.esofthead.mycollab.module.crm.domain.criteria.NoteSearchCriteria;
import com.esofthead.mycollab.test.DataSet;
import com.esofthead.mycollab.test.service.IntergrationServiceTest;

@RunWith(SpringJUnit4ClassRunner.class)
public class NoteServiceTest extends IntergrationServiceTest {

	@Autowired
	protected NoteService noteService;

	@DataSet
	@Test
	public void testGetTotalCount() {
		NoteSearchCriteria criteria = getCriteria();
		assertThat(noteService.getTotalCount(criteria)).isEqualTo(1);
	}

	@SuppressWarnings("unchecked")
	@DataSet
	@Test
	public void testSearch() {
		List<SimpleNote> noteList = noteService
				.findPagableListByCriteria(new SearchRequest<NoteSearchCriteria>(
						getCriteria(), 0, Integer.MAX_VALUE));

		assertThat(noteList.size()).isEqualTo(1);
		assertThat(noteList).extracting("id", "subject", "createduser").contains(
				tuple(1, "aaa", "admin"));
	}

	@DataSet
	@Test
	public void testSaveNote() {
		Note note = new Note();
		note.setSubject("subject");
		note.setSaccountid(1);
		int noteId = noteService.saveWithSession(note, "admin");

		Note note2 = noteService.findByPrimaryKey(noteId, 1);
		assertThat(note2.getSubject()).isEqualTo("subject");
		assertThat(note2.getCreateduser()).isEqualTo("admin");
	}

	private NoteSearchCriteria getCriteria() {
		NoteSearchCriteria criteria = new NoteSearchCriteria();
		criteria.setSaccountid(new NumberSearchField(SearchField.AND, 1));
		return criteria;
	}
}
