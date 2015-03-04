/**
 * This file is part of mycollab-web.
 *
 * mycollab-web is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * mycollab-web is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with mycollab-web.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.esofthead.mycollab.module.project.reporting;

import com.esofthead.mycollab.common.TableViewField;
import com.esofthead.mycollab.core.MyCollabException;
import com.esofthead.mycollab.core.arguments.SearchCriteria;
import com.esofthead.mycollab.core.arguments.SearchRequest;
import com.esofthead.mycollab.core.persistence.service.ISearchableService;
import com.esofthead.mycollab.core.utils.ClassUtils;
import com.esofthead.mycollab.module.project.ProjectLinkBuilder;
import com.esofthead.mycollab.module.project.domain.SimpleTask;
import com.esofthead.mycollab.module.project.domain.SimpleTaskList;
import com.esofthead.mycollab.module.project.view.task.TaskTableFieldDef;
import com.esofthead.mycollab.module.user.AccountLinkBuilder;
import com.esofthead.mycollab.reporting.*;
import com.esofthead.mycollab.reporting.expression.MValue;
import com.esofthead.mycollab.vaadin.AppContext;
import net.sf.dynamicreports.jasper.builder.JasperReportBuilder;
import net.sf.dynamicreports.report.base.expression.AbstractSimpleExpression;
import net.sf.dynamicreports.report.builder.column.ComponentColumnBuilder;
import net.sf.dynamicreports.report.builder.component.*;
import net.sf.dynamicreports.report.builder.style.StyleBuilder;
import net.sf.dynamicreports.report.constant.HorizontalAlignment;
import net.sf.dynamicreports.report.constant.VerticalAlignment;
import net.sf.dynamicreports.report.definition.ReportParameters;
import net.sf.dynamicreports.report.definition.datatype.DRIDataType;
import net.sf.dynamicreports.report.exception.DRException;
import net.sf.jasperreports.engine.JREmptyDataSource;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static net.sf.dynamicreports.report.builder.DynamicReports.*;

/**
 * 
 * @author MyCollab Ltd.
 * @since 1.0
 * 
 */
public class ExportTaskListStreamResource<S extends SearchCriteria> extends ExportItemsStreamResource {
	private static final long serialVersionUID = 1L;

	private static final Logger LOG = LoggerFactory
			.getLogger(ExportTaskListStreamResource.class);

	private ISearchableService<S> searchService;
	private S searchCriteria;

	protected RpParameterBuilder parameters;

	public ExportTaskListStreamResource(String reportTitle,
			ReportExportType outputForm, ISearchableService<S> searchService,
			S searchCriteria) {
		super(AppContext.getUserLocale(), reportTitle, outputForm);
		this.searchCriteria = searchCriteria;
		this.searchService = searchService;
		List<TableViewField> fields = Arrays.asList(TaskTableFieldDef.taskname,
				TaskTableFieldDef.startdate, TaskTableFieldDef.duedate,
				TaskTableFieldDef.assignee);
		this.parameters = new RpParameterBuilder(fields);
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void initReport() throws Exception {
		SearchRequest<S> searchRequest = new SearchRequest<>(searchCriteria,
				0, Integer.MAX_VALUE);
		List<SimpleTaskList> taskLists = searchService.findPagableListByCriteria(searchRequest);

		for (SimpleTaskList taskList : taskLists) {
			VerticalListBuilder componentBuilder = cmp.verticalList();
			StyleBuilder style = stl.style(reportTemplate.getBold12TitleStyle()).setBorder(stl.penThin());

			StyleBuilder styleHyperLink = stl.style(reportTemplate.getBold12TitleStyle()).setBorder(stl.penThin()).setUnderline(true);

			HorizontalListBuilder taskGroupLabel = cmp.horizontalList();

			// TaskList Name
			StyleBuilder taskGroupStyle = stl
					.style(reportTemplate.getBoldStyle())
					.setUnderline(true)
					.setFontSize(12)
					.setAlignment(HorizontalAlignment.CENTER,
							VerticalAlignment.MIDDLE);

			String taskListLink = ProjectLinkBuilder
					.generateTaskGroupPreviewFullLink(taskList.getProjectid(),
							taskList.getId());

			TextFieldBuilder<String> taskListNameHeader = cmp
					.text(taskList.getName()).setFixedWidth(1116)
					.setHorizontalAlignment(HorizontalAlignment.CENTER)
					.setHyperLink(hyperLink(taskListLink))
					.setStyle(taskGroupStyle);
			taskGroupLabel.add(taskListNameHeader).setStyle(
					reportTemplate.getColumnTitleStyle());

			// label
			LOG.debug("Label value : " + taskList.getDescription());
			TextFieldBuilder<String> desLabel = cmp.text("Description :")
					.setStyle(style).setFixedWidth(150);
			TextFieldBuilder<String> description = cmp
					.text(taskList.getDescription()).setFixedWidth(1020)
					.setStyle(style);
			HorizontalListBuilder descContainer = cmp.horizontalList();
			descContainer.add(desLabel).add(description);

			// Assignee
			LOG.debug("Assignee value : " + taskList.getOwnerFullName());
			TextFieldBuilder<String> assigneeLbl = cmp.text("Assignee :")
					.setStyle(style).setFixedWidth(150);
			String assigneeHyperlink = AccountLinkBuilder
					.generatePreviewFullUserLink(taskList.getOwner());
			TextFieldBuilder<String> assignee = cmp
					.text(taskList.getOwnerFullName())
					.setHyperLink(hyperLink(assigneeHyperlink))
					.setStyle(reportTemplate.getUnderlineStyle())
					.setStyle(styleHyperLink).setFixedWidth(435);

			TextFieldBuilder<String> phaseLbl = cmp.text("Phase :")
					.setStyle(style).setFixedWidth(150);

			String phaseHyperLink = ProjectLinkBuilder
					.generateMilestonePreviewFullLink(taskList.getProjectid(),
							taskList.getMilestoneid());
			TextFieldBuilder<String> phase = cmp
					.text(taskList.getMilestoneName())
					.setHyperLink(hyperLink(phaseHyperLink))
					.setStyle(reportTemplate.getUnderlineStyle())
					.setStyle(styleHyperLink).setFixedWidth(435);

			HorizontalListBuilder assigneeAndPhaseHorizontal = cmp
					.horizontalList();
			assigneeAndPhaseHorizontal.add(assigneeLbl).add(assignee)
					.add(phaseLbl).add(phase);

			// Add to Vertical List Builder -------
			componentBuilder.add(taskGroupLabel).add(descContainer)
					.add(assigneeAndPhaseHorizontal);
			SimpleTaskJasperReportBuilder subReportBuilder = new SimpleTaskJasperReportBuilder(
					reportTemplate, taskList.getSubTasks(), parameters);
			if (CollectionUtils.isNotEmpty(taskList.getSubTasks())) {
				componentBuilder.add(subReportBuilder.getSubReportBuilder());
			}
			componentBuilder.add(cmp.horizontalList().setHeight(7));
			reportBuilder.addDetail(componentBuilder);
		}
	}

	private static class SimpleTaskJasperReportBuilder {
		private AbstractReportTemplate reportTemplate;
		private BeanDataSource dataSource;
		private RpParameterBuilder parameters;

		@SuppressWarnings({ "rawtypes", "unchecked" })
		public SimpleTaskJasperReportBuilder(
				AbstractReportTemplate reportTemplate, List data,
				RpParameterBuilder parameters) {
			this.dataSource = new BeanDataSource(data);
			this.parameters = parameters;
			this.reportTemplate = reportTemplate;
		}

		@SuppressWarnings("rawtypes")
		public ComponentBuilder getSubReportBuilder() {
			HorizontalListBuilder horizontalBuilder = cmp.horizontalList();
			horizontalBuilder.setStyle(stl.style(
					reportTemplate.getBoldCenteredStyle()).setBorder(
					stl.penThin()));
			SubreportBuilder subReport = cmp.subreport(new SimpleTaskExpression()).setDataSource(dataSource);
			horizontalBuilder.add(subReport);
			return horizontalBuilder;
		}

		private class SimpleTaskExpression extends AbstractSimpleExpression<JasperReportBuilder> {
			private static final long serialVersionUID = 1L;

			@Override
			public JasperReportBuilder evaluate(ReportParameters param) {
				JasperReportBuilder report = report();
				report.setTemplate(reportTemplate.getReportTemplateBuilder());

				Field[] clsFields = ClassUtils.getAllFields(SimpleTask.class);
				for (Field objField : clsFields) {
					if ("extraData".equals(objField.getName())
							|| "selected".equals(objField.getName())) {
						continue;
					}
					DRIDataType<Object, Object> jrType;
					try {
						jrType = type.detectType(objField.getType().getName());
					} catch (DRException e) {
						throw new MyCollabException("Generate type " + objField.getName(), e);
					}
					report.addField(objField.getName(), jrType);
				}

				List<TableViewFieldDecorator> fields = parameters.getFields();

				Map<String, MValue> builderFields = ColumnBuilderClassMapper.getListFieldBuilder(SimpleTask.class);
				// build columns of report
				for (TableViewFieldDecorator field : fields) {
					LOG.debug("Inject renderer if any");

						MValue columnFieldBuilder = builderFields.get(field
								.getField());
						if (columnFieldBuilder != null) {
							field.setComponentBuilder(reportTemplate
									.buildCompBuilder(columnFieldBuilder));
                            ComponentColumnBuilder columnBuilder = col.componentColumn(
                                    AppContext.getMessage(field.getDescKey()),
                                    field.getComponentBuilder()).setWidth(
                                    field.getDefaultWidth());

                            report.addColumn(columnBuilder);
						} else {
                            LOG.debug("Construct component builder {} and width {}",
                                    field.getField(), field.getDefaultWidth());
                            ComponentColumnBuilder columnBuilder = col.componentColumn(
                                    AppContext.getMessage(field.getDescKey()),
                                    field.getComponentBuilder()).setWidth(
                                    field.getDefaultWidth());

                            report.addColumn(columnBuilder);
                        }
				}
				return report;
			}
		}
	}

	@Override
	protected void fillReport() {
		reportBuilder.setDataSource(new JREmptyDataSource());
	}
}