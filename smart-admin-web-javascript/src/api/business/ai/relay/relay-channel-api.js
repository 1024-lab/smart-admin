/**
 * Relay channel API
 */
import { postRequest, getRequest } from '/@/lib/axios';

export const relayChannelApi = {
  // Page query
  queryPage: (param) => {
    return postRequest('/relay/channel/queryPage', param);
  },
  // List query
  queryList: (param) => {
    return postRequest('/relay/channel/queryList', param);
  },
  // Add
  add: (param) => {
    return postRequest('/relay/channel/add', param);
  },
  // Update
  update: (param) => {
    return postRequest('/relay/channel/update', param);
  },
  // Delete
  delete: (channelId) => {
    return getRequest(`/relay/channel/delete/${channelId}`);
  },
  // Enable or disable
  updateDisabled: (channelId, disabledFlag) => {
    return getRequest(`/relay/channel/updateDisabled/${channelId}/${disabledFlag}`);
  },
  // Bind models
  bindModels: (param) => {
    return postRequest('/relay/channel/bindModels', param);
  },
  // Query bound model ids
  getBoundModelIds: (channelId) => {
    return getRequest(`/relay/channel/boundModelIds/${channelId}`);
  },
};
