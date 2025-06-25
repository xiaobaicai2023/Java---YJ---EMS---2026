  <!--
 * 功能：工单管理
 * 作者：曹晓桐
 * 日期：2023-11-7
-->
<template>
  <div>
    <a-card class="content">
      <!-- 查询条件 start-->
      <a-row align="center">
        <a-col :span="16" style="margin-top: 4px;">
          <a-form :model="searchModel" :label-col-props="{ span: 6 }" :wrapper-col-props="{ span: 18 }"
                  label-align="left" auto-label-width>
            <a-row :gutter="16" align="center" justify="center">
              <!-- <a-col :span="8">
                <a-form-item field="stationType" label="站点类型">
                  <a-select v-model="searchModel.stationType" placeholder="请选择站点类型" allow-clear>
                    <a-option :key="0" label="全部类型" :value="Number(-100)" />
                    <a-option v-for="dict in sys_station_type" :key="dict.value" :label="dict.label"
                      :value="Number(dict.value)" />
                  </a-select>
                </a-form-item>
              </a-col> -->

              <!--能源类型-->
              <a-col :span="12">
                <a-form-item field="stationType" :label="$t('global.energy')">
                  <a-select v-model="searchModel.stationType" :placeholder="$t('global.pleaseSelect')"
                            allow-clear>
                    <a-option :key="0" :label="'全部类型'" :value="Number(-100)"/>
                    <a-option v-for="dict in sys_station_type" :key="dict.value" :label="dict.label"
                              :value="Number(dict.value)"/>
                  </a-select>
                </a-form-item>
              </a-col>
              <a-col :span="12">
                <a-form-item field="orderTitle" :label="$t('pv.maintenance.list.orderName')">
                  <a-input v-model="searchModel.orderTitle" :placeholder="$t('global.fuzzySearch')" allow-clear/>
                </a-form-item>
              </a-col>
            </a-row>
            <a-row :gutter="16" align="center">
              <a-col :span="12">
                <a-form-item :label="$t('pv.maintenance.list.orderStatus')">
                  <a-select v-model="searchModel.status" placeholder="请选择工单状态" allow-clear>
                    <a-option :key="0" label="全部状态" :value="Number(-100)"/>
                    <a-option v-for="dict in sys_order_status" :key="dict.value" :label="dict.label"
                              :value="Number(dict.value)"/>
                  </a-select>
                </a-form-item>
              </a-col>
              <a-col :span="12">
                <a-form-item field="time" :label="$t('pv.maintenance.list.orderTime')">
                  <a-range-picker v-model="searchModel.time" style="width:100%"/>
                </a-form-item>
              </a-col>
            </a-row>
          </a-form>
        </a-col>
        <a-divider style="height: 84px;margin-bottom: 12px" direction="vertical"/>
        <a-col :span="2" style="text-align: right;padding-bottom: 12px">
          <a-space :size="18">
            <a-button type="primary" @click="search">
              <template #icon>
                <icon-search/>
              </template>
              {{ $t('global.search') }}
            </a-button>
          </a-space>
        </a-col>
      </a-row>
      <!-- 查询条件 end-->

      <!-- 分割线 -->
      <a-divider style="margin-top: 0"/>

      <!-- 操作按钮 start-->
      <a-row style="margin-bottom: 16px">
        <a-col :span="12">
          <a-space>
            <a-button type="primary" @click="handleAdd">
              <template #icon>
                <icon-plus/>
              </template>
              {{ $t('pv.maintenance.list.newOrder') }}
            </a-button>
          </a-space>
        </a-col>
      </a-row>
      <!-- 操作按钮 end-->

      <!-- 表格内容  -->
      <a-table :scroll="{ x: 1680 }" row-key="id" ref="tableRef" :loading="loading"
               :bordered="{ wrapper: true, cell: true }" :columns="columns" :data="renderData"
               @page-change="onPageChange" :pagination="pagination" @page-size-change="onPageSizeChange">
        <!-- 协作人 -->
        <template #collaborateFor="{ record }">
          {{ record.collaborateFor.map((item: any) => item.name).join(',') }}
        </template>
        <!-- 计划时间 -->
        <template #planTime="{ record }">
          {{ `${record.planStartTime || '-'} ~ ${record.planEndTime || '-'}` }}
        </template>
        <!-- 完成时间 -->
        <template #completeTime="{ record }">
          {{ record.completeTime || '-' }}
        </template>
        <!-- 超时状态 -->
        <template #timeoutStatus="{ record }">
          <dict-tag :options="sys_timeout_status" :value="record.timeoutStatus"/>
        </template>
        <!-- 超时状态 -->
        <template #status="{ record }">
          <dict-tag :options="sys_order_status" :value="record.status"/>
        </template>
        <template #operate="{ record }">
          <a-button v-if="!(record.status == 11 || record.status == 10) && record.status > -1" size="small" type="text"
                    status="success" @click="handleStatus(record.id)">{{ $t('pv.maintenance.list.handle') }}
          </a-button>
          <a-button size="small" type="text" status="success" @click="handleUpdate(record.id)">{{
              $t('global.edit')
            }}
          </a-button>
          <a-button size="small" type="text" status="success" @click="handleDetail(record.id)">{{
              $t('global.detail')
            }}
          </a-button>
          <a-button size="small" type="text" status="danger" @click="handleDelete(record)">{{ $t('global.delete') }}
          </a-button>
        </template>
      </a-table>

      <!-- 添加右弹层 start -->
      <a-drawer :width="1000" :esc-to-close="false" :ok-loading="formDrawer.loading" :visible="formDrawer.visible"
                @ok="handleSubmitForm"
                @cancel="handleFormCancel" :mask-closable="false">
        <template #title>
          {{ formDrawer.formModel.id ? $t('pv.maintenance.list.editOrder') : $t('pv.maintenance.list.newOrder') }}
        </template>
        <a-spin :loading="formDrawer.loading" style="width: 100%;height: auto;" :tip="$t('global.loading')">
          <a-form ref="formCreateRef" auto-label-width :model="formDrawer.formModel" label-align="right"
                  :rules="formDrawer.rules">
            <a-row :gutter="24">
              <a-col :span="14" style="height:100vh;border-right: 1px solid var(--color-border);">

                <!-- 工单名称 -->
                <a-form-item field="orderTitle" :label="$t('pv.maintenance.list.orderName')">
                  <a-input v-model="formDrawer.formModel.orderTitle"/>
                </a-form-item>
                <!-- 站点类型 -->
                <a-form-item field="stationType" :label="$t('pv.maintenance.list.siteType')">
                  <a-select v-model="formDrawer.formModel.stationType"
                            :placeholder="$t('pv.maintenance.list.pleaseSelectSite')" allow-clear>
                    <a-option v-for="dict in sys_station_type" :key="dict.value" :label="dict.label"
                              :value="Number(dict.value)"></a-option>
                  </a-select>
                </a-form-item>
                <!-- 选择站点 -->
                <a-form-item field="stationId" :label="$t('pv.maintenance.list.selectSite')" :content-flex="false"
                             :merge-props="false">
                  <a-row :gutter="8">
                    <a-col :span="12">
                      <a-form-item field="stationId" no-style>
                        <a-tree-select allow-search allow-clear="false" v-model="formDrawer.formModel.stationId"
                                       :data="renderStationData" @change="handleStationChange"
                                       :placeholder="$t('pv.maintenance.list.selectSite')"
                                       :fieldNames="{ key: 'id', title: 'deptName', children: 'children' }"
                                       :filter-tree-node="filterTreeNode"></a-tree-select>
                      </a-form-item>
                    </a-col>
                    <a-col :span="12">
                      <a-form-item field="deviceId" no-style>
                        <a-tree-select allow-clear allow-search v-model="formDrawer.formModel.deviceId"
                                       :fieldNames="{ key: 'key', title: 'deviceName', children: 'children' }"
                                       :data="renderDeviceData" :filter-tree-node="filterDeviceTreeNode"
                                       @change="handleDeviceChange"
                                       :placeholder="$t('pv.maintenance.list.pleaseSelectDevice')"></a-tree-select>
                      </a-form-item>
                    </a-col>
                  </a-row>
                </a-form-item>

                <!-- 关联报警 -->
                <a-form-item field="alarmId" :label="$t('pv.maintenance.list.correlationAlarm')">
                  <a-select v-model="formDrawer.formModel.alarmId" :options="renderTriggerData" allow-clear
                            :placeholder="$t('pv.maintenance.list.alarmEvent')"
                            :fieldNames="{ value: 'id', label: 'triggerName' }">
                  </a-select>
                </a-form-item>

                <!-- 优先级 -->
                <a-form-item field="priorityLevel" :label="$t('pv.maintenance.list.priorityLevel')">
                  <a-select v-model="formDrawer.formModel.priorityLevel" allow-clear
                            :placeholder="$t('pv.maintenance.list.selectPriorityLevel')">
                    <a-option v-for="dict in sys_priority" :key="dict.value" :label="dict.label"
                              :value="dict.value"></a-option>
                  </a-select>
                </a-form-item>

                <!-- 计划时间 -->
                <a-form-item field="planTime" :label="$t('pv.maintenance.list.planTime')">
                  <a-range-picker v-model="formDrawer.formModel.planTime" show-now-btn format="YYYY-MM-DD HH:mm"
                                  show-time @change="onPlanTimeChange"/>
                </a-form-item>

                <!-- 工单类型 -->
                <a-form-item field="orderType" :label="$t('pv.maintenance.list.orderType')"
                             :validate-trigger="['change', 'blur', 'input']">
                  <a-radio-group v-model="formDrawer.formModel.orderType">
                    <a-radio v-for="dict in sys_order_type" :key="dict.value" :value="Number(dict.value)">{{
                        dict.label
                      }}
                    </a-radio>
                  </a-radio-group>
                </a-form-item>

                <!-- 工单描述 -->
                <a-form-item field="orderContent" :label="$t('pv.maintenance.list.orderDesc')">
                  <a-textarea :placeholder="$t('pv.maintenance.list.pleaseInputOrderDesc')" :max-length="200"
                              v-model="formDrawer.formModel.orderContent" allow-clear/>
                </a-form-item>

                <!-- 图片描述  :custom-request="customRequest"-->
                <a-form-item field="fileList" :label="$t('pv.maintenance.list.pictureDesc')">
                  <a-upload accept="image/*" :download="true" list-type="picture-card"
                            :file-list="formDrawer.formModel.orderFilesImage" image-preview
                            :action="(`${baseUrl}/file/upload`)"
                            :headers="headers" @success="handleUploadSuccess" @change="handleUploadChange"/>
                </a-form-item>

              </a-col>
              <a-col :span="10">
                <a-divider orientation="left">{{ $t('pv.maintenance.list.flow') }}</a-divider>
                <a-form-item hide-label>
                  <a-timeline>
                    <a-timeline-item dot-color="var(--color-fill-4)">
                      {{ $t('pv.maintenance.list.creator') }}
                      <template #label>
                        <a-tag color="rgb(var(--primary-6))">{{ formDrawer.formModel.createBy }}</a-tag>
                      </template>
                    </a-timeline-item>
                    <a-timeline-item dot-color="var(--color-fill-4)">
                      {{ $t('pv.maintenance.list.principal') }}
                      <template #label>
                        <a-space wrap>
                          <a-tag v-if="formDrawer.formModel.chargeBy && formDrawer.formModel.chargeBy.length > 0"
                                 color="rgb(var(--primary-6))" closable @close="handleUserRemove(1, null)">{{
                              formDrawer.formModel.chargeBy
                            }}
                          </a-tag>
                          <a-tag v-else @click="handleUserAdd(1, '负责人')">
                            <template #icon>
                              <icon-plus/>
                            </template>
                            {{ $t('pv.maintenance.list.add') }}
                          </a-tag>
                        </a-space>
                      </template>
                    </a-timeline-item>
                    <a-timeline-item dot-color="var(--color-fill-4)">
                      {{ $t('pv.maintenance.list.cooperate') }}
                      <template #label>
                        <a-space wrap>
                          <a-tag v-if="formDrawer.formModel.collaborateFor.length > 0"
                                 v-for="(item, index) in formDrawer.formModel.collaborateFor"
                                 :key="item.name"
                                 color="rgb(var(--primary-6))"
                                 closable @close="handleUserRemove(2, index)">{{ item.name }}
                          </a-tag>
                          <a-tag v-else @click="handleUserAdd(2, '协作人')">
                            <template #icon>
                              <icon-plus/>
                            </template>
                            添加
                          </a-tag>
                        </a-space>
                      </template>
                    </a-timeline-item>
                    <a-timeline-item dot-color="var(--color-fill-4)">
                      {{ $t('pv.maintenance.list.cc') }}
                      <template #label>
                        <a-space wrap>
                          <a-tag v-if="formDrawer.formModel.copyFor.length > 0"
                                 v-for="(item, index) in formDrawer.formModel.copyFor" color="rgb(var(--primary-6))"
                                 :key="item.name"
                                 closable
                                 @close="handleUserRemove(3, index)">{{ item.name }}
                          </a-tag>
                          <a-tag v-else @click="handleUserAdd(3, '抄送人')">
                            <template #icon>
                              <icon-plus/>
                            </template>
                            {{ $t('pv.maintenance.list.add') }}
                          </a-tag>
                        </a-space>
                      </template>
                    </a-timeline-item>
                    <a-timeline-item dot-color="var(--color-fill-4)">
                      {{ $t('pv.maintenance.list.verifier') }}
                      <template #label>
                        <a-space wrap>
                          <a-tag v-if="formDrawer.formModel.verifyFor.length > 0"
                                 v-for="(item, index) in formDrawer.formModel.verifyFor" color="rgb(var(--primary-6))"
                                 :key="item.name"
                                 closable
                                 @close="handleUserRemove(4, index)">{{ item.name }}
                          </a-tag>
                          <a-tag v-else @click="handleUserAdd(4, '验证人')">
                            <template #icon>
                              <icon-plus/>
                            </template>
                            {{ $t('pv.maintenance.list.add') }}
                          </a-tag>
                        </a-space>
                      </template>
                    </a-timeline-item>
                  </a-timeline>
                </a-form-item>
              </a-col>
            </a-row>
          </a-form>
        </a-spin>
      </a-drawer>
      <!-- 添加右弹层 end -->


      <!-- 工单详情 start -->
      <a-drawer :width="1000" :esc-to-close="false" :visible="formDrawer.detailVisible" @ok="handleFormCancel"
                @cancel="handleFormCancel" :mask-closable="false">
        <template #title>
          {{ $t('pv.maintenance.list.orderDetail') }}
        </template>
        <a-spin :loading="formDrawer.loading" style="width: 100%;height: auto;" :tip="$t('global.loading')">
          <a-row :gutter="24">
            <a-col :span="14" style="height:auto;border-right: 1px solid var(--color-border);">

              <a-divider orientation="left">{{ $t('pv.maintenance.list.orderInfo') }}</a-divider>
              <a-descriptions :align="{ label: 'right' }" :label-style="{ color: 'rgb(var(--color-neutral-10))' }"
                              :column="1" size="large">
                <!-- 工单名称 -->
                <a-descriptions-item :label="`${$t('pv.maintenance.list.orderName')}:`">
                  <a-skeleton v-if="formDrawer.loading" :animation="true">
                    <a-skeleton-line :rows="1"/>
                  </a-skeleton>
                  <span v-else>{{ formDrawer.formModel.orderTitle }}</span>
                </a-descriptions-item>

                <!-- 站点名称 -->
                <a-descriptions-item :label="`${$t('manage.account.authority.siteName')}:`">
                  <a-skeleton v-if="formDrawer.loading" :animation="true">
                    <a-skeleton-line :rows="1"/>
                  </a-skeleton>
                  <span v-else>{{ formDrawer.formModel.stationName || '-' }}</span>
                </a-descriptions-item>

                <!-- 关联事件 -->
                <a-descriptions-item :label="`${$t('pv.maintenance.list.associatedEvent')}:`">
                  <a-skeleton v-if="formDrawer.loading" :animation="true">
                    <a-skeleton-line :rows="1"/>
                  </a-skeleton>
                  <span v-else>{{ formDrawer.formModel.alarmName || '-' }}</span>
                </a-descriptions-item>

                <!-- 优先级 -->
                <a-descriptions-item :label="`${$t('pv.maintenance.list.priorityLevel')}:`">
                  <a-skeleton v-if="formDrawer.loading" :animation="true">
                    <a-skeleton-line :rows="1"/>
                  </a-skeleton>
                  <dict-tag v-else :options="sys_priority" :value="formDrawer.formModel.priorityLevel"/>
                </a-descriptions-item>

                <!-- 计划时间 -->
                <a-descriptions-item :label="`${$t('pv.maintenance.list.planTime')}:`">
                  <a-skeleton v-if="formDrawer.loading" :animation="true">
                    <a-skeleton-line :rows="1"/>
                  </a-skeleton>
                  <span v-else> {{
                      `${formDrawer.formModel.planStartTime || '-'} ~ ${formDrawer.formModel.planEndTime ||
                      '-'}`
                    }}</span>
                </a-descriptions-item>

                <!-- 负责人 -->
                <a-descriptions-item :label="`${$t('pv.maintenance.list.principal')}:`">
                  <a-skeleton v-if="formDrawer.loading" :animation="true">
                    <a-skeleton-line :rows="1"/>
                  </a-skeleton>
                  <template v-else>
                    <a-space>
                      <a-tag v-if="formDrawer.formModel.chargeBy" color="rgb(var(--primary-6))">{{
                          formDrawer.formModel.chargeBy
                        }}
                      </a-tag>
                      <a-tag @click="handleUserTransfer" :style="{
          backgroundColor: 'var(--color-fill-2)',
          border: '1px dashed var(--color-fill-3)',
          cursor: 'pointer',
        }">
                        <template #icon>
                          <icon-redo/>
                        </template>
                        {{ $t('pv.maintenance.list.transfer') }}
                      </a-tag>
                    </a-space>
                  </template>
                </a-descriptions-item>

                <!-- 协作人 -->
                <a-descriptions-item label="协作人:">
                  <a-skeleton v-if="formDrawer.loading" :animation="true">
                    <a-skeleton-line :rows="1"/>
                  </a-skeleton>
                  <template v-else>
                    <a-space>
                      <a-tag v-if="formDrawer.formModel.collaborateFor.length > 0"
                             v-for="(item, index) in formDrawer.formModel.collaborateFor" color="rgb(var(--primary-6))">
                        {{
                          item.name
                        }}
                      </a-tag>
                    </a-space>
                  </template>
                </a-descriptions-item>

                <!-- 抄送人 -->
                <a-descriptions-item :label="`${$t('pv.maintenance.list.cc')}:`">
                  <a-skeleton v-if="formDrawer.loading" :animation="true">
                    <a-skeleton-line :rows="1"/>
                  </a-skeleton>
                  <template v-else>
                    <a-space>
                      <a-tag v-if="formDrawer.formModel.copyFor.length > 0"
                             v-for="(item, index) in formDrawer.formModel.copyFor" color="rgb(var(--primary-6))">{{
                          item.name
                        }}
                      </a-tag>
                    </a-space>
                  </template>
                </a-descriptions-item>

                <!-- 工单描述 -->
                <a-descriptions-item :label="`${$t('pv.maintenance.list.orderDesc')}:`">
                  <a-skeleton v-if="formDrawer.loading" :animation="true">
                    <a-skeleton-line :rows="1"/>
                  </a-skeleton>
                  <span v-else>{{ formDrawer.formModel.orderContent || '-' }}</span>
                </a-descriptions-item>

                <!-- 图片描述 -->
                <a-descriptions-item :label="`${$t('pv.maintenance.list.pictureDesc')}:`">
                  <a-skeleton v-if="formDrawer.loading" :animation="true">
                    <a-skeleton-line :rows="1"/>
                  </a-skeleton>
                  <a-upload disabled
                            v-else-if="formDrawer.formModel.orderFilesImage && formDrawer.formModel.orderFilesImage.length > 0"
                            list-type="picture-card" :limit="formDrawer.formModel.orderFilesImage.length"
                            :default-file-list="formDrawer.formModel.orderFilesImage" image-preview/>
                </a-descriptions-item>

              </a-descriptions>

              <a-divider orientation="left">{{ $t('pv.maintenance.list.mHandle') }}</a-divider>
              <a-descriptions :align="{ label: 'right' }" :label-style="{ color: 'rgb(var(--color-neutral-10))' }"
                              :column="1" size="large">

                <!-- 接单时间 -->
                <a-descriptions-item :label="`${$t('pv.maintenance.list.receivingTime')}:`">
                  <a-skeleton v-if="formDrawer.loading" :animation="true">
                    <a-skeleton-line :rows="1"/>
                  </a-skeleton>
                  <span v-else>{{ formDrawer.formModel.receiveTime }}</span>
                </a-descriptions-item>

                <!-- 开始时间 -->
                <a-descriptions-item :label="`${$t('pv.maintenance.list.startTime')}:`">
                  <a-skeleton v-if="formDrawer.loading" :animation="true">
                    <a-skeleton-line :rows="1"/>
                  </a-skeleton>
                  <span v-else>{{ formDrawer.formModel.startTime }}</span>
                </a-descriptions-item>

                <!-- 完成时间 -->
                <a-descriptions-item :label="`${$t('pv.maintenance.list.endTime')}:`">
                  <a-skeleton v-if="formDrawer.loading" :animation="true">
                    <a-skeleton-line :rows="1"/>
                  </a-skeleton>
                  <span v-else>{{ formDrawer.formModel.completeTime }}</span>
                </a-descriptions-item>

                <!-- 实际用时 -->
                <a-descriptions-item :label="`${$t('pv.maintenance.list.actualTime')}:`">
                  <a-skeleton v-if="formDrawer.loading" :animation="true">
                    <a-skeleton-line :rows="1"/>
                  </a-skeleton>
                  <span v-else>{{
                      formDrawer.formModel.useMinutes ? `${formDrawer.formModel.useMinutes}分钟` : ''
                    }}</span>
                </a-descriptions-item>

                <!-- 处理过程 -->
                <a-descriptions-item :label="`${$t('pv.maintenance.list.mProcess')}:`">
                  <a-skeleton v-if="formDrawer.loading" :animation="true">
                    <a-skeleton-line :rows="1"/>
                  </a-skeleton>
                  <span v-else>{{ formDrawer.formModel.handleProcess }}</span>
                </a-descriptions-item>

                <!-- 现场照片 -->
                <a-descriptions-item :label="`${$t('pv.maintenance.list.mPicture')}:`">
                  <a-skeleton v-if="formDrawer.loading" :animation="true">
                    <a-skeleton-line :rows="1"/>
                  </a-skeleton>
                  <a-upload disabled
                            v-else-if="formDrawer.formModel.handleFilesImage && formDrawer.formModel.handleFilesImage.length > 0"
                            list-type="picture-card" :limit="formDrawer.formModel.handleFilesImage.length"
                            :default-file-list="formDrawer.formModel.handleFilesImage" image-preview/>
                </a-descriptions-item>
              </a-descriptions>
              <a-divider orientation="left">{{ $t('pv.maintenance.list.orderMessage') }}</a-divider>

              <!-- 留言列表 -->
              <template v-for="(item, index) in formDrawer.formModel.leaveMessageList" :key="index">
                <a-comment :author="item.userName" :datetime="item.replyTime" align="right">
                  <template #avatar>
                    <a-avatar :style="{ backgroundColor: 'rgb(var(--primary-6))' }">
                      {{ item.userName }}
                    </a-avatar>
                  </template>
                  <template #content>
                    <a-space direction="vertical" fill>
                      <div>
                        {{ item.replyContent }}
                      </div>
                      <a-upload disabled v-if="item.replyFilesImg && item.replyFilesImg.length > 0"
                                list-type="picture-card" :limit="item.replyFilesImg.length"
                                :default-file-list="item.replyFilesImg" image-preview/>
                    </a-space>
                  </template>
                </a-comment>
              </template>
              <!-- 新增留言 -->
              <div style="margin-left: 56px;margin-top: 20px;">
                <a-form ref="formLeaveMessageRef" auto-label-width :model="formDrawer.formLeaveMessageModel"
                        label-align="right" @submit="handleLeaveMessageSubmit">
                  <a-form-item field="replyContent" hide-label>
                    <a-input v-model:model-value="formDrawer.formLeaveMessageModel.replyContent"
                             :placeholder="$t('pv.maintenance.list.pleaseMessage')"></a-input>
                  </a-form-item>
                  <a-form-item field="fileList" hide-label>
                    <a-upload :tip="$t('pv.maintenance.list.clickUpload')" accept="image/*" list-type="picture-card"
                              :action="(`${baseUrl}/file/upload`)"
                              :file-list="formDrawer.formLeaveMessageModel.fileList"
                              image-preview :headers="headers" @change="handleUploadChange1"/>
                  </a-form-item>
                  <a-form-item hide-label>
                    <a-button html-type="submit" type="primary"
                              :loading="formDrawer.formLeaveMessageModel.loading">
                      {{ $t('pv.maintenance.list.submitMessage') }}
                    </a-button>
                  </a-form-item>
                </a-form>
              </div>

              <a-divider orientation="left">{{ $t('pv.maintenance.list.systemInfo') }}</a-divider>
              <a-descriptions :align="{ label: 'right' }" :label-style="{ color: 'rgb(var(--color-neutral-10))' }"
                              :column="1" size="large">

                <!-- 创建人员 -->
                <a-descriptions-item :label="`${$t('pv.maintenance.list.createS')}:`">
                  <a-skeleton v-if="formDrawer.loading" :animation="true">
                    <a-skeleton-line :rows="1"/>
                  </a-skeleton>
                  <span v-else>{{ formDrawer.formModel.createBy }}</span>
                </a-descriptions-item>

                <!-- 创建时间 -->
                <a-descriptions-item :label="`${$t('pv.maintenance.list.createTime')}:`">
                  <a-skeleton v-if="formDrawer.loading" :animation="true">
                    <a-skeleton-line :rows="1"/>
                  </a-skeleton>
                  <span v-else>{{ formDrawer.formModel.createTime }}</span>
                </a-descriptions-item>

                <!-- 更新人员 -->
                <a-descriptions-item :label="`${$t('pv.maintenance.list.updateS')}:`">
                  <a-skeleton v-if="formDrawer.loading" :animation="true">
                    <a-skeleton-line :rows="1"/>
                  </a-skeleton>
                  <span v-else>{{ formDrawer.formModel.updateBy }}</span>
                </a-descriptions-item>

                <!-- 更新时间 -->
                <a-descriptions-item :label="`${$t('pv.maintenance.list.updateTime')}:`">
                  <a-skeleton v-if="formDrawer.loading" :animation="true">
                    <a-skeleton-line :rows="1"/>
                  </a-skeleton>
                  <span v-else>{{ formDrawer.formModel.updateTime }}</span>
                </a-descriptions-item>
              </a-descriptions>

            </a-col>
            <a-col :span="10">
              <a-divider orientation="left">{{ $t('pv.maintenance.list.orderProgress') }}</a-divider>
              <a-steps :current="formDrawer.formModel.orderProcessIndex" direction="vertical">
                <a-step v-for="item in formDrawer.formModel.orderProcess" :status="item.status">{{ item.title }}
                  <template #description>
                    <a-space direction="vertical" fill>
                      {{ item.time }}
                      <a-tag v-if="item.userName" color="rgb(var(--primary-6))">{{ item.userName }}</a-tag>
                    </a-space>
                  </template>
                </a-step>
              </a-steps>
            </a-col>
          </a-row>
        </a-spin>
      </a-drawer>
      <!-- 工单详情 end -->

      <!-- 操作弹框 start -->
      <a-modal width="450px" v-model:visible="operateModalModel.visible" class="modal-box">
        <template #title>
          <icon-close-circle v-if="operateModalModel.type == 1" size="18"
                             style="color: rgb(var(--red-6)); margin-right: 5px"/>
          <icon-exclamation-circle v-else size="18" style="color: rgb(var(--orange-6)); margin-right: 5px"/>
          确认提示
        </template>
        <div style="text-align: center;">是否确认{{ operateModalModel.title }}名称为【{{
            operateModalModel.name
          }}】的数据项？
        </div>
        <template #footer>
          <div style="text-align: center">
            <a-space>
              <a-button type="primary" status="danger" @click="handleOperateModelCancle">取消</a-button>
              <a-button type="primary" @click="handleOperateModelOk">确认</a-button>
            </a-space>
          </div>
        </template>
      </a-modal>
      <!-- 操作弹框 end -->

      <!-- 选择人员弹框  start-->
      <a-modal width="600px" :visible="modalModel.visible" :footer="false" title-align="start" :title="modalModel.title"
               @cancel="handleModalClose">
        <a-form :model="modalModel.serarchModel" auto-label-width>
          <a-row>
            <a-col>
              <a-form-item field="catalogId" label="人员名称">
                <a-input v-model="modalModel.serarchModel.nickName"/>
                <a-button type="primary" @click="userSearch">
                  <template #icon>
                    <icon-search/>
                  </template>
                  查询
                </a-button>
              </a-form-item>
            </a-col>
            <a-col>
              <a-table :loading="modalModel.loading" :scroll="{ y: 500 }" row-key="id"
                       :bordered="{ wrapper: true, cell: true }" :columns="userTabColumns"
                       :data="modalModel.renderData" @page-change="onUserPageChange" :pagination="userPagination"
                       @page-size-change="onUserSizeChange">
                <template #operate="{ record }">
                  <a-button v-if="record.isAdd != 1" size="small" type="primary"
                            @click="handleUserSelect(record)">选择
                  </a-button>
                </template>
              </a-table>
            </a-col>
          </a-row>
        </a-form>
      </a-modal>
      <!-- 选择人员弹框  end-->


      <!-- 工单处理 start 工单状态（-1已取消 0新工单 1已接单 2进行中 3已完成 10已验证 20延迟处理）-->
      <a-modal width="450px" v-model:visible="formDrawer.statusVisible" class="modal-box" title="工单处理">
        <div>
          <a-form ref="formRef" auto-label-width :model="formDrawer.formModel" label-align="right"
                  :rules="formDrawer.rules">
            <!-- 工单名称 -->
            <a-form-item field="orderTitle" label="工单名称:">
              {{ formDrawer.formModel.orderTitle }}
            </a-form-item>
            <!-- 当前环节 -->
            <a-form-item field="status" label="当前环节:">
              <dict-tag :options="sys_order_status" :value="formDrawer.formModel.status"/>
            </a-form-item>
            <!-- 处理过程 -->
            <a-form-item v-if="formDrawer.formModel.status == 2" field="handleProcess" label="处理过程:">
              <a-textarea placeholder="请输入处理过程，最多不超过200字" :max-length="200"
                          v-model="formDrawer.formModel.handleProcess"
                          allow-clear/>
            </a-form-item>
            <!-- 现场照片 -->
            <a-form-item v-if="formDrawer.formModel.status == 2" field="fileList" label="现场照片:">
              <a-upload accept="image/*" :download="true" list-type="picture-card" :action="(`${baseUrl}/file/upload`)"
                        :file-list="formDrawer.fileList" image-preview :headers="headers"
                        @change="handleUploadChange3"/>
            </a-form-item>
            <!-- 验证意见 -->
            <a-form-item v-if="formDrawer.formModel.status == 3" field="verifyAdvice" label="验证意见:">
              <a-textarea placeholder="请输入验证意见，最多不超过200字" :max-length="200"
                          v-model="formDrawer.formModel.verifyAdvice"
                          allow-clear/>
            </a-form-item>
          </a-form>
        </div>
        <template #footer>
          <div style="text-align: right">
            <a-space>
              <a-button v-if="formDrawer.formModel.status == 0" type="primary" status="danger"
                        @click="handleOrderStatus(-1)">取消工单
              </a-button>
              <a-button v-if="formDrawer.formModel.status == 1" type="primary" status="danger"
                        @click="handleOrderStatus(20)">延迟处理
              </a-button>
              <a-button v-if="formDrawer.formModel.status == 0" type="primary"
                        @click="handleOrderStatus(1)">接受工单
              </a-button>
              <a-button v-if="formDrawer.formModel.status == 1" type="primary"
                        @click="handleOrderStatus(2)">处理工单
              </a-button>
              <a-button v-if="formDrawer.formModel.status == 2" type="primary"
                        @click="handleOrderStatus(3)">完成工单
              </a-button>
              <a-button v-if="formDrawer.formModel.status == 3" type="primary" status="danger"
                        @click="handleOrderStatus(11)">未通过验证
              </a-button>
              <a-button v-if="formDrawer.formModel.status == 3" type="primary"
                        @click="handleOrderStatus(10)">通过验证
              </a-button>
            </a-space>
          </div>
        </template>
      </a-modal>
      <!-- 工单处理 end-->

    </a-card>
  </div>
</template>

<script setup lang="ts">
import {computed, getCurrentInstance, onMounted, reactive, ref} from 'vue';
import useLoading from '@/hooks/loading';
import {FileItem, TableInstance} from "@arco-design/web-vue";
import {BasePagination} from '@/api/common';
import {FormInstance} from '@arco-design/web-vue/es/form';
import dayjs from 'dayjs';
import {addOrder, getOrder, listOrder, updateOrder, delOrder} from '@/api/system/order';
import {JsonCommonVo, getStationList} from '@/api/public';
import {getTrigger, listTrigger, listTriggerAll} from '@/api/system/trigger';
import {getToken} from '@/utils/auth';
import {useUserStore} from '@/store'
import {listUser} from '@/api/manage/account/user';
import {StationTypeEnum, listFusionGroup} from '@/api/system/device';
import {findById, processSelectable, processSelectableByCompany} from '@/utils/ruoyi';
import {addLeaveMessage, listLeaveMessage} from '@/api/system/leave-message';
import {notification} from "@/hooks/my-design";
import {useRoute, useRouter} from "vue-router";

/*************************** 变量区域 begin ***************************/

//接受组件参数
const props = defineProps({
  stationType: {
    type: Number,
    default: StationTypeEnum.power,
  },
})

//******* 这里编写动态获取下拉列表 start ******
const proxy = getCurrentInstance()?.appContext.config.globalProperties;
const {
  sys_normal_disable,
  sys_order_status,
  sys_timeout_status,
  sys_priority,
  sys_station_type,
  sys_order_type
} = proxy?.useDict("sys_normal_disable", "sys_order_status", "sys_timeout_status", "sys_priority", "sys_station_type", 'sys_order_type');
//******* 这里编写动态获取下拉列表 end ******


const userStore = useUserStore();
const router = useRouter();
const route = useRoute();
// 获取当前时间
var dateNow = dayjs().format("YYYY-MM-DD");
var dateBefore = dayjs().subtract(1, 'month').format("YYYY-MM-DD");

//生成查询条件对象
const generateSearchModel: any = () => {
  return {
    stationType: props.stationType,
    orderTitle: "",
    status: -100,
    time: [dateBefore, dateNow],
  };
};

//查询表单对象
const searchModel = ref(generateSearchModel());
//加载中
const {loading, setLoading} = useLoading(true);
//表格分页参数
const pagination: any = reactive({...BasePagination()});
//选择用户分页
const userPagination: any = reactive({...BasePagination()});
//表格实例
const tableRef = ref<TableInstance>();
//设置表格列
const columns = computed<any[]>(() => [
  {
    title: "工单名称",
    dataIndex: "orderTitle",
    align: 'left',
    fixed: 'left',
    width: 200,
    ellipsis: true,
    tooltip: {position: 'top'},
  },
  {
    title: "站点&设备",
    dataIndex: "stationName",
    width: 250,
    ellipsis: true,
    tooltip: {position: 'top'},
  },
  {
    title: "事件名称",
    dataIndex: "alarmName",
    width: 200,
    ellipsis: true,
    tooltip: {position: 'top'},
  },
  {
    title: "负责人",
    dataIndex: "chargeBy",
    align: 'center',
    width: 120
  },
  {
    title: "协作人",
    dataIndex: "collaborateFor",
    slotName: "collaborateFor",
    align: 'left',
    width: 200,
    ellipsis: true,
    tooltip: {position: 'top'},
  },
  {
    title: "创建时间",
    dataIndex: "createTime",
    slotName: "createTime",
    width: 180,
    align: 'center'
  },
  {
    title: "计划时间",
    dataIndex: "planTime",
    slotName: "planTime",
    width: 360,
    align: 'center'
  },
  {
    title: "完成时间",
    dataIndex: "completeTime",
    slotName: "completeTime",
    width: 180,
    align: 'center'
  },
  {
    title: "超时状态",
    dataIndex: "timeoutStatus",
    slotName: "timeoutStatus",
    align: 'center',
    width: 120,
    fixed: 'right'
  },
  {
    title: "工单状态",
    dataIndex: "status",
    slotName: "status",
    align: 'center',
    width: 120,
    fixed: 'right'
  },
  {
    title: "操作",
    dataIndex: "operations",
    width: 280,
    slotName: 'operate',
    align: 'left',
    fixed: 'right'
  },
]);
// 成员列
const userTabColumns: any[] = [
  {
    title: "姓名",
    dataIndex: "nickName",
    width: 150
  },
  {
    title: "部门",
    dataIndex: "dept.deptName",
    width: 250
  },
  {
    title: "操作",
    dataIndex: "operations",
    width: 100,
    slotName: 'operate',
    align: 'center',
  },
];
//表格数据
const renderData = ref<any[]>([]);
//报警事件列表
const renderTriggerData = ref<any>([]);
//站点列表
const renderStationData = ref<any>([]);
//设备列表
const renderDeviceData = ref<any>([])
//操作弹框
const generateOperateModalModel = () => {
  return {
    //0 状态 1删除
    type: 0,
    visible: false,
    title: "",
    id: 0,
    value: 0,
    name: ""
  };
};
//操作弹框模型
const operateModalModel = ref(generateOperateModalModel());
//表格实例
const formRef = ref<FormInstance>();
const formCreateRef = ref<FormInstance>();
//留言表格实例
const formLeaveMessageRef = ref<FormInstance>();
//生成表单模型
const generateFormDrawerModel = () => {
  return {
    visible: false,
    statusVisible: false,
    detailVisible: false,
    loading: false,
    rules: {
      orderTitle: [{required: true, message: "请输入工单名称"}],
      stationType: [{required: true, message: "请选择站点类型"}],
      stationId: [{required: true, message: "请选择站点"}],
      deviceId: [{required: true, message: "请选择设备"}],
      // alarmId: [{ required: true, message: "请选择报警事件" }],
      priorityLevel: [{required: true, message: "请选择优先级别"}],
      planTime: [{required: true, message: "请选择计划时间"}],
      orderContent: [{required: true, message: "请输入工单描述"}],
      orderType: [{required: true, message: "请选择工单类型"}]
    },
    fileList: [] as FileItem[],
    formLeaveMessageModel: {
      loading: false,
      recordId: undefined,
      replyContent: undefined,
      replyFiles: [] as JsonCommonVo[],
      fileList: [] as FileItem[]
    },
    formModel: {
      id: 0,
      status: 0,
      stationType: props.stationType,
      timeoutStatus: 0,
      orderTitle: '',
      orderContent: '',
      stationId: undefined,
      stationName: undefined,
      deviceId: undefined,
      deviceName: undefined,
      alarmId: undefined,
      alarmName: undefined,
      priorityLevel: undefined,
      planTime: undefined,
      //计划开始时间
      planStartTime: '',
      //计划结束时间
      planEndTime: '',
      //图片描述
      orderFiles: [] as JsonCommonVo[],
      orderFilesImage: [] as FileItem[],
      //现场照片
      handleFiles: [] as JsonCommonVo[],
      handleFilesImage: [] as FileItem[],
      //创建人
      createBy: '',
      createTime: '',
      updateBy: '',
      updateTime: '',
      //负责任
      chargeBy: '',
      //协作人
      collaborateFor: [] as JsonCommonVo[],
      //抄送人
      copyFor: [] as JsonCommonVo[],
      //验证人
      verifyFor: [] as JsonCommonVo[],
      //接单时间
      receiveTime: undefined,
      //开始时间
      startTime: undefined,
      //完成时间
      completeTime: undefined,
      //实际用时
      useMinutes: undefined,
      //处理过程
      handleProcess: undefined,
      //验证意见
      verifyAdvice: undefined,
      //留言
      leaveMessageList: [] as any[],
      orderProcess: [] as any[],
      orderProcessIndex: -1,
      //工单类型
      orderType: 1,
    }
  };
};
//表单模型
const formDrawer = ref(generateFormDrawerModel());

//选择变量弹框模型
const generateModalModel = () => {
  return {
    visible: false,
    loading: false,
    title: '',
    //1、负责人 2、协作人 3、抄送人 4、验证人
    userType: 1,
    //搜索条件
    serarchModel: {
      nickName: "",
    },
    //表格数据
    renderData: []
  }
}
//弹框模型
const modalModel = ref(generateModalModel());
/*************************** 变量区域 end ***************************/


/*************************** 方法区域 begin ***************************/

//重置查询条件
const search = async () => {
  pagination.current = 1;
  await fetchData();
}

/**
 * 新增数据
 * @param row 数据行
 */
const handleAdd = async () => {
  formDrawer.value = generateFormDrawerModel();
  await fetchStationData();
  formDrawer.value.formModel.createBy = userStore.user.nickName;
  formDrawer.value.visible = true;
}

/**
 * 站点搜索
 * @param searchValue
 * @param nodeData
 */
const filterTreeNode = (searchValue: any, nodeData: any) => {
  return nodeData.deptName.toLowerCase().indexOf(searchValue.toLowerCase()) > -1;
}

/**
 * 站点搜索
 * @param searchValue
 * @param nodeData
 */
const filterDeviceTreeNode = (searchValue: any, nodeData: any) => {
  return nodeData.deviceName.toLowerCase().indexOf(searchValue.toLowerCase()) > -1;
}

/**
 * 选择人员-打开
 */
const handleUserAdd = async (userType: number, title: string) => {
  modalModel.value = generateModalModel();
  modalModel.value.visible = true;
  modalModel.value.userType = userType;
  modalModel.value.title = `选择${title}`;
  userPagination.current = 1;
  await fetchUserData();
}

/**
 * 转派-打开
 */
const handleUserTransfer = async () => {
  modalModel.value = generateModalModel();
  modalModel.value.visible = true;
  modalModel.value.userType = 6;
  modalModel.value.title = `工单转派`;
  userPagination.current = 1;
  await fetchUserData();
}

/**
 * 选择人员-关闭
 */
const handleModalClose = () => {
  modalModel.value = generateModalModel();
}

/**
 * 用户搜索
 */
const userSearch = async () => {
  userPagination.current = 1;
  await fetchUserData();
}

/**
 * 负责人移除
 */
const handleUserRemove = async (userType: number, index: any) => {
  if (userType == 1) {
    //负责人
    formDrawer.value.formModel.chargeBy = '';
  } else if (userType == 2) {
    //协作人 collaborateFor
    formDrawer.value.formModel.collaborateFor.splice(index, 1);
  } else if (userType == 3) {
    //抄送人 copyFor
    formDrawer.value.formModel.copyFor.splice(index, 1);
  } else if (userType == 4) {
    //验证人 verifyFor
    formDrawer.value.formModel.verifyFor.splice(index, 1);
  }

}


//表格页码发生变化
const onPageChange = (val: number) => {
  pagination.current = val;
  fetchData();
}

//表格每页数量发生变化
const onPageSizeChange = (val: number) => {
  pagination.pageSize = val;
  fetchData();
}


/**
 * 表格页码发生变化-用户列表
 * @param val
 */
const onUserPageChange = async (val: number) => {
  userPagination.current = val;
  await fetchUserData();
}

/**
 * 表格每页数量发生变化-用户列表
 * @param val 值
 */
const onUserSizeChange = async (val: number) => {
  userPagination.pageSize = val;
  await fetchUserData();
}

/**
 * 选中
 * userType 1、负责人 2、协作人 3、抄送人 4、验证人
 * @param record
 */
const handleUserSelect = async (record: any) => {
  // console.log("handleUserSelect", record)
  let userType = modalModel.value.userType;
  if (userType == 1) {
    //负责人
    formDrawer.value.formModel.chargeBy = record.nickName || '';
  } else if (userType == 2) {
    // //协作人 collaborateFor
    addUser(record, formDrawer.value.formModel.collaborateFor);
  } else if (userType == 3) {
    //抄送人 copyFor
    addUser(record, formDrawer.value.formModel.copyFor);
  } else if (userType == 4) {
    //验证人 verifyFor
    addUser(record, formDrawer.value.formModel.verifyFor);
  } else if (userType == 6) {
    formDrawer.value.formModel.chargeBy = record.nickName || '';
    let result = await updateOrder(formDrawer.value.formModel);
    notification(result);
    modalModel.value = generateModalModel();
  }
}

/**
 * 添加用户
 * @param record
 * @param data
 */
const addUser = (record: any, data: JsonCommonVo[]) => {
  const isObjectInArray = data.some((obj: JsonCommonVo) => {
    return Number(obj.value) === record.id;
  });
  if (!isObjectInArray) {
    data.push({
      name: record.nickName,
      value: record.id
    })
  }
}

/**
 * 删除空数据
 * @param data
 */
const removeEmptyChildren = (data: any) => {
  data.forEach((item: any, index: number) => {
    if (item.children && item.children.length === 0) {
      delete data[index].children;
    } else if (item.children) {
      removeEmptyChildren(item.children);
    }
  });
}

/**
 * 编辑数据
 * @param row 数据行
 */
const handleUpdate = async (val: any) => {
  formDrawer.value.visible = true;
  formDrawer.value.loading = true;
  try {
    let res = await getOrder(val);
    if (res.code == 200) {
      res.data.planTime = [dayjs(res.data.planStartTime), dayjs(res.data.planEndTime)];
      //图片描述
      if (res.data.orderFiles && res.data.orderFiles.length > 0) {
        let orderFiles: FileItem[] = [];
        res.data.orderFiles.forEach((item: any, index: number) => {
          orderFiles.push({
            uid: index.toString(),
            name: item.name,
            url: item.value,
          })
        })
        res.data.orderFilesImage = orderFiles;
      }
      formDrawer.value.formModel = res.data;
      await fetchStationData();
      await fetchTriggerData();
      await fetchDeviceData();
    }
  } catch (e) {

  } finally {
    formDrawer.value.loading = false;
  }

}

/**
 * 处理工单
 * @param val
 */
const handleStatus = async (val: any) => {
  formDrawer.value.statusVisible = true;
  formDrawer.value.loading = true;
  try {
    let res = await getOrder(val);
    if (res.code == 200) {
      formDrawer.value.formModel = res.data;
    }
  } catch (e) {

  } finally {
    formDrawer.value.loading = false;
  }

}

/**
 * 修改订单状态
 * 工单状态（-1已取消 0新工单 1已接单 2进行中 3已完成 10已验证 20延迟处理）
 */
const handleOrderStatus = async (val: any) => {
  formDrawer.value.loading = true;
  try {
    let param: any = formDrawer.value.formModel;
    if (val == 1) {
      param.receiveTime = dayjs().format("YYYY-MM-DD HH:mm:ss");
    }

    if(val == 2){
      param.startTime = dayjs().format("YYYY-MM-DD HH:mm:ss");
    }

    if (val == 3) {
      param.completeTime = dayjs().format("YYYY-MM-DD HH:mm:ss");
      let fileList = formDrawer.value.fileList;
      let handleFiles: JsonCommonVo[] = [];
      if (fileList && fileList.length > 0) {
        fileList.forEach((item, index) => {
          handleFiles.push({
            name: item.name || '',
            value: item.url || ''
          })
        })
        param.handleFiles = handleFiles;
      }

    }
    let result = await updateOrder({...formDrawer.value.formModel, status: val});
    notification(result);
    if (result.code == 200) {
      handleFormCancel();
      await fetchData();
    }
  } catch (e) {

  } finally {
    formDrawer.value.loading = false;
  }
}

/**
 * 编辑数据
 * @param row 数据行
 */
const handleDetail = async (val: any) => {
  // console.log(formRef.value)
  formDrawer.value.detailVisible = true;
  formDrawer.value.loading = true;
  try {
    let res = await getOrder(val);
    if (res.code == 200) {

      //图片描述
      if (res.data.orderFiles && res.data.orderFiles.length > 0) {
        let orderFiles: FileItem[] = [];
        res.data.orderFiles.forEach((item: any, index: number) => {
          orderFiles.push({
            uid: index.toString(),
            name: item.name,
            url: item.value,
          })
        })
        res.data.orderFilesImage = orderFiles;

      }

      //  处理图标
      if (res.data.handleFiles && res.data.handleFiles.length > 0) {
        let handleFiles: FileItem[] = [];
        res.data.handleFiles.forEach((item: any, index: number) => {
          handleFiles.push({
            uid: index.toString(),
            name: item.name,
            url: item.value,
          })
        })
        res.data.handleFilesImage = handleFiles;

      }

      //工单状态（-1已取消 0新工单 1已接单 2进行中 3已完成 10已验证 20延迟处理）
      let orderProcessIndex = -1;
      switch (res.data.status) {
        case 0:
          orderProcessIndex = 1;
          break;
        case 1:
          orderProcessIndex = 2;
          break;
        case 2:
          orderProcessIndex = 3;
          break;
        case 3:
          orderProcessIndex = 4;
          break;
        case 10:
          orderProcessIndex = 6;
          break;
        case 11:
          orderProcessIndex = 6;
          break;
        case 20:
          orderProcessIndex = 2;
          break;
        default:
          orderProcessIndex = -1;
      }
      res.data.orderProcessIndex = orderProcessIndex;
      res.data.orderProcess = [
        {
          index: 1,
          time: res.data.createTime,
          title: "创建工单",
          userName: res.data.createBy,
        },
        {
          index: 2,
          time: res.data.receiveTime,
          title: "接受工单",
          userName: '',
        },
        {
          index: 3,
          time: undefined,
          title: "处理工单",
          userName: '',
        },
        {
          index: 4,
          time: res.data.completeTime,
          title: "完成工单",
          userName: '',
        },
        {
          index: 5,
          time: res.data.verifyTime,
          title: "验证工单",
          userName: res.data.verifyFor.map((item: any) => {
            return item.name
          }).join(","),
          status: res.data.status == 11 ? 'error' : ""
        }
      ]
      formDrawer.value.formModel = res.data;
      await fetchTriggerInfo(res.data.alarmId);
      await fetchLeaveMessageData();

    }
  } catch (e) {
    console.error(e)
  } finally {
    formDrawer.value.loading = false;
  }

}

/**
 * 删除数据
 * @param record 数据行
 */
const handleDelete = (record: any) => {
  operateModalModel.value.visible = true;
  operateModalModel.value.id = record.id;
  operateModalModel.value.title = '删除';
  operateModalModel.value.name = record.orderTitle;
  operateModalModel.value.type = 1;
}

/**
 * 删除提示弹框取消
 */
const handleOperateModelCancle = () => {
  operateModalModel.value = generateOperateModalModel();
}

/**
 * 删除提示弹框确认
 */
const handleOperateModelOk = async () => {
  operateModalModel.value.visible = false;
  setLoading(true);
  let result = await delOrder(operateModalModel.value.id);
  notification(result);
  if (result.code == 200) {
    handleOperateModelCancle();
    await fetchData();
  }
}

/**
 * 提交留言
 */
const handleLeaveMessageSubmit = async () => {
  formDrawer.value.formLeaveMessageModel.loading = true;
  let replyContent = formDrawer.value.formLeaveMessageModel.replyContent;
  if (!replyContent || replyContent <= 0) {
    notification({code: 201, msg: "请输入留言"});
    formDrawer.value.formLeaveMessageModel.loading = false;
    return;
  }
  try {
    let param = {
      recordId: formDrawer.value.formModel.id,
      replyContent: replyContent,
      replyFiles: formDrawer.value.formLeaveMessageModel.replyFiles,
      replyTime: dayjs().format("YYYY-MM-DD HH:mm:ss"),
      userId: userStore.user.id,
      userName: userStore.user.nickName,
      tableName: 'devops_order'
    }
    let res = await addLeaveMessage(param);
    notification(res);
    if (res.code == 200) {
      formLeaveMessageRef.value?.resetFields()
      formDrawer.value.formLeaveMessageModel.fileList = [];
      await fetchLeaveMessageData();
    }
  } catch (e) {
    console.error(e);
  } finally {
    formDrawer.value.formLeaveMessageModel.loading = false;
  }
}
/**
 * 提交表单
 */
const handleSubmitForm = async () => {
  const validate = await formCreateRef.value?.validate();
  if (!validate) {
    formDrawer.value.loading = true;
    try {
      let result;
      if (formDrawer.value.formModel.id == 0) {
        result = await addOrder(formDrawer.value.formModel);
      } else {
        result = await updateOrder(formDrawer.value.formModel);
      }
      notification(result);
      if (result.code == 200) {
        handleFormCancel();
        await fetchData();
      }
    } catch (e) {
      console.error(e);
    } finally {
      formDrawer.value.loading = false;
    }
  }
}

/**
 * 表单取消
 */
const handleFormCancel = () => {
  formDrawer.value = generateFormDrawerModel();
  router.replace({
    path: route.path,
    query: {}, // 清空查询参数
  });
  formCreateRef.value?.resetFields();
}

/**
 * 计划时间-改变
 * @param dateString
 * @param date
 */
const onPlanTimeChange = (dateString: any, date: any) => {
  // console.log('onPlanTimeChange: ', dateString, date);
  formDrawer.value.formModel.planStartTime = dayjs(dateString[0]).format("YYYY-MM-DD HH:mm:00");
  formDrawer.value.formModel.planEndTime = dayjs(dateString[1]).format("YYYY-MM-DD HH:mm:00");
  // console.log('onPlanTimeChange: ', formDrawer.value.formModel.planStartTime, formDrawer.value.formModel.planEndTime);
}

//路径
const baseUrl = import.meta.env.VITE_API_BASE_URL;
//请求头
const headers: Record<string, string> = {
  'Authorization': `Bearer ${getToken()}`
}

/**
 * 上传成功
 * @param fileItem
 */
const handleUploadSuccess = (fileItem: FileItem) => {
  console.log("handleUploadSuccess", fileItem, formDrawer.value.fileList)
}

/**
 * 上传成功
 * @param fileItem
 */
const handleUploadChange3 = (fileList: FileItem[], fileItem: FileItem) => {
  if (fileItem.status == 'done') {
    let list: JsonCommonVo[] = [];
    fileList.forEach((item: FileItem, index) => {
      list.push({
        name: item.response ? item.response.data.name : item.name,
        value: item.response ? item.response.data.url : item.url,
      });
    })
    formDrawer.value.formModel.handleFiles = list;
  }
}

/**
 * 上传成功
 * @param fileItem
 */
const handleUploadChange = (fileList: FileItem[], fileItem: FileItem) => {
  if (fileItem.status == 'done') {
    // formDrawer.value.fileList = fileList;
    let list: JsonCommonVo[] = [];
    fileList.forEach((item: FileItem, index) => {
      list.push({
        name: item.response ? item.response.data.name : item.name,
        value: item.response ? item.response.data.url : item.url,
      });
    })
    formDrawer.value.formModel.orderFiles = list;
  }
}
/**
 * 上传成功
 * @param fileItem
 */
const handleUploadChange1 = (fileList: FileItem[], fileItem: FileItem) => {
  if (fileItem.status == 'done') {
    let list: JsonCommonVo[] = [];
    fileList.forEach((item: FileItem, index) => {
      list.push({
        name: item.response ? item.response.data.name : item.name,
        value: item.response ? item.response.data.url : item.url,
      });
    })
    formDrawer.value.formLeaveMessageModel.replyFiles = list;
  }
}

/**
 * 站点发生改变
 * @param record
 */
const handleStationChange = async (val: any) => {
  const info = findById(renderStationData.value, val);
  if (info) {
    formDrawer.value.formModel.stationId = info.id;
    formDrawer.value.formModel.deviceId = undefined;
    renderTriggerData.value = [];
    formDrawer.value.formModel.alarmId = undefined;
    await fetchDeviceData();
  }
}

/**
 * 设备发生改变
 * @param record
 */
const handleDeviceChange = async (val: any) => {
  const info = findById(renderDeviceData.value, val);
  if (info) {
    renderTriggerData.value = [];
    formDrawer.value.formModel.alarmId = undefined;
    formDrawer.value.formModel.deviceId = info.id;
    await fetchTriggerData();
  }
}

/**
 * 获取留言
 */
const fetchLeaveMessageData = async () => {
  let resMsg: any = await listLeaveMessage({
    tableName: "devops_order",
    recordId: formDrawer.value.formModel.id,
    pageSize: 10000,
    pageNum: 1,
  });
  if (resMsg.code == 200) {
    if (resMsg.rows && resMsg.rows.length > 0) {
      resMsg.rows.forEach((item: any, index: number) => {
        if (item.replyFiles && item.replyFiles.length > 0) {
          let imgList: any[] = [];
          item.replyFiles.forEach((item1: any, index: number) => {
            imgList.push({
              uid: index.toString(),
              name: item1.name,
              url: item1.value,
            })
          })
          item.replyFilesImg = imgList;
        }
      })
    }
    formDrawer.value.formModel.leaveMessageList = resMsg.rows;
  }
}

/**
 * 获取报警事件列表
 */
const fetchUserData = async () => {
  modalModel.value.loading = true;
  try {
    const res = await listUser({
      pageSize: userPagination.pageSize,
      pageNum: userPagination.current,
      ...modalModel.value.serarchModel,
    });
    if (res.code == 200) {
      modalModel.value.renderData = res.rows;
      userPagination.total = res.total;
    }
  } catch (err) {
  } finally {
    modalModel.value.loading = false;
  }
}


/**
 * 获取站点信息
 */
const fetchStationData = async () => {
  try {
    let res = await getStationList({});
    if (res.code == 200) {
      processSelectableByCompany(res.data)
      renderStationData.value = res.data;
    }
  } catch (e) {

  } finally {

  }
}

/**
 * 获取设备列表
 */
const fetchDeviceData = async () => {
  try {
    const res = await listFusionGroup({
      deptId: formDrawer.value.formModel.stationId,
      stationType: props.stationType,
    });
    processSelectable(res.data);
    renderDeviceData.value = res.data;
  } catch (err) {
    // you can report use errorHandler or other
  } finally {
  }
}


/**
 * 获取报警事件列表
 */
const fetchTriggerData = async () => {
  try {
    const res = await listTriggerAll({deviceId: formDrawer.value.formModel.deviceId, triggerStatus: 0});
    if (res.code == 200) {
      renderTriggerData.value = res.data;
    }
  } catch (err) {
  } finally {
  }
}

/**
 * 获取报警事件列表
 */
const fetchTriggerInfo = async (val: any) => {
  try {
    const res = await getTrigger(val);
    if (res.code == 200) {
      formDrawer.value.formModel.alarmName = res.data.triggerName
    }
  } catch (err) {
  } finally {
  }
}

/**
 * 查询表格数据
 */
const fetchData = async () => {
  setLoading(true);
  try {
    const params = {
      ...searchModel.value,
      pageSize: pagination.pageSize,
      pageNum: pagination.current,
      maintainTitle: searchModel.value.maintainTitle,
      stationType: searchModel.value.stationType == -100 ? undefined : searchModel.value.stationType,
      status: searchModel.value.status == -100 ? undefined : searchModel.value.status,
    };

    delete params.time;

    if (searchModel.value.time) {
      params['params[beginTime]'] = dayjs(searchModel.value.time[0]).format("YYYY-MM-DD 00:00:00");
      params['params[endTime]'] = dayjs(searchModel.value.time[1]).format("YYYY-MM-DD 23:59:59");
    }
    const res: any = await listOrder(params);
    renderData.value = res.rows;
    pagination.total = res.total;
  } catch (err) {
    console.error("工单管理-错误", err)
  } finally {
    setLoading(false);
  }
};

/*************************** 方法区域 end ***************************/

onMounted(async () => {
  await fetchData();
  tableRef.value?.expandAll(true);

  if(route.query.deviceId){
    formDrawer.value.visible = true;
    try{
      formDrawer.value.loading = true;
      await fetchStationData();
      await fetchTriggerData();
      await fetchDeviceData();

      formDrawer.value.formModel.createBy = userStore.user.nickName;
      formDrawer.value.formModel.stationType = Number(route.query.stationType);
      formDrawer.value.formModel.stationId = Number(route.query.deptId);
      formDrawer.value.formModel.deviceId = Number(route.query.deviceId);
      formDrawer.value.formModel.alarmId = Number(route.query.id);
    }catch(e){
      console.error(e);
    }finally {
      formDrawer.value.loading = false;
    }
  }
})
</script>

<style scoped>
.list-row {
  display: flex;
}
</style>
