/*******************************************************************************
 * Copyright (C) 2018 Andrej Podhradsky
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 ******************************************************************************/
package org.qetools.task_generator;

import java.io.File;
import java.io.IOException;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ErrorCollector;

public abstract class AbstractTaskGeneratorTest {

	private String project;

	public AbstractTaskGeneratorTest(String project) {
		this.project = project;
	}

	@Rule
	public ErrorCollector collector = new ErrorCollector();

	@Test
	public void testGeneratingEpic() throws Exception {
		generate("template-epic.yaml");
		assertNumberOfIssues(1);
		assertIssue(0, project + "-1", "Epic", "Epic 1", "user1", "1.0", null, null);
	}

	@Test
	public void testGeneratingEpicRetry() throws Exception {
		generate("template-epic.yaml");
		generate("template-epic.yaml");
		assertNumberOfIssues(1);
		assertIssue(0, project + "-1", "Epic", "Epic 1", "user1", "1.0", null, null);
	}

	@Test
	public void testGeneratingEpicUpdate() throws Exception {
		generate("template-epic.yaml");
		assertNumberOfIssues(1);
		assertIssue(0, project + "-1", "Epic", "Epic 1", "user1", "1.0", null, null);
		generate("template-epic-update.yaml");
		assertNumberOfIssues(2);
		assertIssue(0, project + "-1", "Epic", "Epic 1", "user1", "1.0", null, null);
		assertIssue(1, project + "-2", "Epic", "Epic 2", "user2", "2.0", null, null);
	}

	@Test
	public void testGeneratingEpicAndTask() throws Exception {
		generate("template-epic-task.yaml");
		assertNumberOfIssues(2);
		assertIssue(0, project + "-1", "Epic", "Epic 1", "user1", "1.0", null, null);
		assertIssue(1, project + "-2", "Task", "Task 11", "user11", "1.0", project + "-1", null);
	}

	@Test
	public void testGeneratingEpicAndTaskRetry() throws Exception {
		generate("template-epic-task.yaml");
		generate("template-epic-task.yaml");
		assertNumberOfIssues(2);
		assertIssue(0, project + "-1", "Epic", "Epic 1", "user1", "1.0", null, null);
		assertIssue(1, project + "-2", "Task", "Task 11", "user11", "1.0", project + "-1", null);
	}

	@Test
	public void testGeneratingEpicAndTaskUpdate() throws Exception {
		generate("template-epic-task.yaml");
		assertNumberOfIssues(2);
		assertIssue(0, project + "-1", "Epic", "Epic 1", "user1", "1.0", null, null);
		assertIssue(1, project + "-2", "Task", "Task 11", "user11", "1.0", project + "-1", null);
		generate("template-epic-task-update.yaml");
		assertNumberOfIssues(3);
		assertIssue(0, project + "-1", "Epic", "Epic 1", "user1", "1.0", null, null);
		assertIssue(1, project + "-2", "Task", "Task 11", "user11", "1.0", project + "-1", null);
		assertIssue(2, project + "-3", "Task", "Task 12", "user12", "1.0", project + "-1", null);
	}

	@Test
	public void testGeneratingEpicAndTaskAndSubtask() throws Exception {
		generate("template-epic-task-subtask.yaml");
		assertNumberOfIssues(3);
		assertIssue(0, project + "-1", "Epic", "Epic 1", "user1", "1.0", null, null);
		assertIssue(1, project + "-2", "Task", "Task 11", "user11", "1.0", project + "-1", null);
		assertIssue(2, project + "-3", "Sub-task", "Subtask 111", "user111", "1.0", null, project + "-2");
	}

	@Test
	public void testGeneratingEpicAndTaskAndSubtaskRetry() throws Exception {
		generate("template-epic-task-subtask.yaml");
		assertNumberOfIssues(3);
		assertIssue(0, project + "-1", "Epic", "Epic 1", "user1", "1.0", null, null);
		assertIssue(1, project + "-2", "Task", "Task 11", "user11", "1.0", project + "-1", null);
		assertIssue(2, project + "-3", "Sub-task", "Subtask 111", "user111", "1.0", null, project + "-2");
	}

	@Test
	public void testGeneratingEpicAndTaskAndSubtaskUpdate() throws Exception {
		generate("template-epic-task-subtask.yaml");
		assertNumberOfIssues(3);
		assertIssue(0, project + "-1", "Epic", "Epic 1", "user1", "1.0", null, null);
		assertIssue(1, project + "-2", "Task", "Task 11", "user11", "1.0", project + "-1", null);
		assertIssue(2, project + "-3", "Sub-task", "Subtask 111", "user111", "1.0", null, project + "-2");
		generate("template-epic-task-subtask-update.yaml");
		assertNumberOfIssues(4);
		assertIssue(0, project + "-1", "Epic", "Epic 1", "user1", "1.0", null, null);
		assertIssue(1, project + "-2", "Task", "Task 11", "user11", "1.0", project + "-1", null);
		assertIssue(2, project + "-3", "Sub-task", "Subtask 111", "user111", "1.0", null, project + "-2");
		assertIssue(3, project + "-4", "Sub-task", "Subtask 112", "user112", "1.0", null, project + "-2");
	}

	@Test
	public void testGeneratingEpicAndSubtask() throws Exception {
		generate("template-epic-subtask.yaml");
		assertNumberOfIssues(2);
		assertIssue(0, project + "-1", "Epic", "Epic 1", "user1", "1.0", null, null);
		assertIssue(1, project + "-2", "Sub-task", "Subtask 11", "user11", "1.0", null, project + "-1");
	}

	@Test
	public void testGeneratingEpicAndSubtaskRetry() throws Exception {
		generate("template-epic-subtask.yaml");
		assertNumberOfIssues(2);
		assertIssue(0, project + "-1", "Epic", "Epic 1", "user1", "1.0", null, null);
		assertIssue(1, project + "-2", "Sub-task", "Subtask 11", "user11", "1.0", null, project + "-1");
	}

	@Test
	public void testGeneratingEpicAndSubtaskUpdate() throws Exception {
		generate("template-epic-subtask.yaml");
		generate("template-epic-subtask-update.yaml");
		assertNumberOfIssues(3);
		assertIssue(0, project + "-1", "Epic", "Epic 1", "user1", "1.0", null, null);
		assertIssue(1, project + "-2", "Sub-task", "Subtask 11", "user11", "1.0", null, project + "-1");
		assertIssue(2, project + "-3", "Sub-task", "Subtask 12", "user12", "1.0", null, project + "-1");
	}

	@Test
	public void testGeneratingTask() throws Exception {
		generate("template-task.yaml");
		assertNumberOfIssues(1);
		assertIssue(0, project + "-1", "Task", "Task 1", "user1", null, null, null);
	}

	@Test
	public void testGeneratingTaskRetry() throws Exception {
		generate("template-task.yaml");
		generate("template-task.yaml");
		assertNumberOfIssues(1);
		assertIssue(0, project + "-1", "Task", "Task 1", "user1", null);
		assertIssue(0, project + "-1", "Task", "Task 1", "user1", null, null, null);
	}

	@Test
	public void testGeneratingTaskUpdate() throws Exception {
		generate("template-task.yaml");
		assertNumberOfIssues(1);
		assertIssue(0, project + "-1", "Task", "Task 1", "user1", null, null, null);
		generate("template-task-update.yaml");
		assertNumberOfIssues(2);
		assertIssue(0, project + "-1", "Task", "Task 1", "user1", null, null, null);
		assertIssue(1, project + "-2", "Task", "Task 2", "user2", null, null, null);
	}

	@Test
	public void testGeneratingTaskAndSubtask() throws Exception {
		generate("template-task-subtask.yaml");
		assertNumberOfIssues(2);
		assertIssue(0, project + "-1", "Task", "Task 1", "user1", null, null, null);
		assertIssue(1, project + "-2", "Sub-task", "Subtask 11", "user11", null, null, project + "-1");
	}

	@Test
	public void testGeneratingTaskAndSubtaskRetry() throws Exception {
		generate("template-task-subtask.yaml");
		assertNumberOfIssues(2);
		assertIssue(0, project + "-1", "Task", "Task 1", "user1", null, null, null);
		assertIssue(1, project + "-2", "Sub-task", "Subtask 11", "user11", null, null, project + "-1");
	}

	@Test
	public void testGeneratingTaskAndSubtaskUpdate() throws Exception {
		generate("template-task-subtask.yaml");
		assertNumberOfIssues(2);
		assertIssue(0, project + "-1", "Task", "Task 1", "user1", null, null, null);
		assertIssue(1, project + "-2", "Sub-task", "Subtask 11", "user11", null, null, project + "-1");
		generate("template-task-subtask-update.yaml");
		assertNumberOfIssues(3);
		assertIssue(0, project + "-1", "Task", "Task 1", "user1", null, null, null);
		assertIssue(1, project + "-2", "Sub-task", "Subtask 11", "user11", null, null, project + "-1");
		assertIssue(2, project + "-3", "Sub-task", "Subtask 12", "user12", null, null, project + "-1");
	}

	protected abstract void assertNumberOfIssues(int expectedNumberOfIssues);

	protected abstract void assertIssue(int index, String expectedKey, String expectedIssueType, String expectedSummary,
			String expectedAssignee, String expectedVersion);

	protected abstract void assertIssue(int index, String expectedKey, String expectedIssueType, String expectedSummary,
			String expectedAssignee, String expectedVersion, String expectedEpic, String expectedParent);

	protected File getFile(String fileName) {
		return new File(getClass().getClassLoader().getResource(fileName).getFile());
	}

	protected void generate(String yamlFile) throws IOException {
		generate(yamlFile, null);
	}

	protected abstract void generate(String yamlFile, String configFile) throws IOException;

}
