// 任务调度API
import {
  postAxios,
  getAxios
} from '@/lib/http';
export const taskApi = {
  // 查询任务列表
  getTaskList: (data) => {
    return postAxios('/quartz/task/query', data);
  },
  // 添加或更新任务
  addOrUpdateTask: (data) => {
    return postAxios('/quartz/task/saveOrUpdate', data);
  },
  // 查询任务日志
  getTaskLog: (data) => {
    return postAxios('/quartz/task/queryLog', data);
  },
  // 暂停任务
  updateTaskPause: (taskId) => {
    return getAxios(`/quartz/task/pause/${taskId}`);
  },
  // 运行任务
  updateTaskRun: (taskId) => {
    return getAxios(`/quartz/task/run/${taskId}`);
  },
  //   恢复任务
  updateTaskResume: (taskId) => {
    return getAxios(`/quartz/task/resume/${taskId}`);
  },
  //   删除任务
  deleteTasks: (taskId) => {
    return getAxios(`/quartz/task/delete/${taskId}`);
  }
};
