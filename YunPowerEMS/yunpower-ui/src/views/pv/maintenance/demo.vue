<template>
	<div class="container">

		<a-card class="general-card" title="动态表头">

			<a-radio-group v-model="position" type="button" @change="positionChange">
				<a-radio value="day">天</a-radio>
				<a-radio value="hour">小时</a-radio>
			</a-radio-group>

			<a-table :data="data">
				<template #columns>
					<a-table-column v-for="item in customColumnsValue" :title="item.title"
						:data-index="item.dataIndex"></a-table-column>
					<a-table-column title="操作">
						<template #cell="{ record }">
							<a-button>查看</a-button>
						</template>
					</a-table-column>
				</template>
			</a-table>
		</a-card>

		<a-row>
			<a-col :flex="6">
				<a-card title="用电能耗统计 在头部" class="energy-box">
					<template #extra>
						<time-bar @change="timeChange" :pickType="0" />
					</template>
					<electricity-chart />
				</a-card>
			</a-col>
			<a-col :flex="6">
				<a-card title="用电能耗统计 在内容里" class="energy-box">
					<time-bar @change="timeChange" :pickType="1" />
					<electricity-chart />
				</a-card>
			</a-col>
		</a-row>

		<a-divider style="margin-top: 0" />

		<a-card title="曹晓桐的自定义图标">
			<custom-icon type="icon-ykite-dianli"></custom-icon>
		</a-card>
		<a-divider style="margin-top: 0" />

		<a-card title="十七的自定义图标">
			<custom-icon type="复制的代码"></custom-icon>
		</a-card>
		<a-divider style="margin-top: 0" />

		<a-card title="张怡静的自定义图标">
			<custom-icon type="复制的代码"></custom-icon>
		</a-card>
		<a-divider style="margin-top: 0" />

		<a-card title="杨季鸿的自定义图标">
			<custom-icon type="复制的代码"></custom-icon>
		</a-card>
		<a-divider style="margin-top: 0" />

		<a-card title="周孟琦的自定义图标">
			<custom-icon type="icon-ykite-add-copy"></custom-icon>
			<custom-icon type="icon-ykite-wj-jlb"></custom-icon>
		</a-card>
		<a-divider style="margin-top: 0" />

		<a-card class="general-card" title="查询表格">

			<!-- 查询条件 start-->
			<a-row>
				<a-col :flex="1" style="margin-top: 4px;">
					<a-col :flex="1">
						<a-form :model="formModel" :label-col-props="{ span: 6 }" :wrapper-col-props="{ span: 18 }"
							label-align="left">
							<a-row :gutter="16">
								<a-col :span="8">
									<a-form-item field="trueName" label="用户姓名">
										<a-input v-model="formModel.trueName" placeholder="请输入用户姓名" allow-clear />
									</a-form-item>
								</a-col>
								<a-col :span="8">
									<a-form-item field="phone" label="用户电话">
										<a-input v-model="formModel.phone" placeholder="请输入用户电话" allow-clear />
									</a-form-item>
								</a-col>
								<a-col :span="8">
									<a-form-item field="phone" label="用户电话">
										<a-input v-model="formModel.phone" placeholder="请输入用户电话" allow-clear />
									</a-form-item>
								</a-col>
								<a-col :span="8">
									<a-form-item field="phone" label="用户电话">
										<a-input v-model="formModel.phone" placeholder="请输入用户电话" allow-clear />
									</a-form-item>
								</a-col>
								<a-col :span="8">
									<a-form-item field="phone" label="用户电话">
										<a-input v-model="formModel.phone" placeholder="请输入用户电话" allow-clear />
									</a-form-item>
								</a-col>
								<a-col :span="8">
									<a-form-item field="phone" label="用户电话">
										<a-input v-model="formModel.phone" placeholder="请输入用户电话" allow-clear />
									</a-form-item>
								</a-col>
							</a-row>
						</a-form>
					</a-col>
				</a-col>
				<a-divider style="height: 84px" direction="vertical" />
				<a-col :flex="'86px'" style="text-align: right">
					<a-space direction="vertical" :size="18">
						<a-button type="primary" @click="search">
							<template #icon>
								<icon-search />
							</template>
							查询
						</a-button>
						<a-button @click="reset">
							<template #icon>
								<icon-refresh />
							</template>
							重置
						</a-button>
					</a-space>
				</a-col>
			</a-row>
			<!-- 查询条件 end-->

			<a-divider style="margin-top: 0" />

			<!-- 表格 start -->
			<a-table row-key="id" :loading="loading" :pagination="pagination" :columns="columns"
				:data="renderData" :bordered="false" @page-change="onPageChange" @page-size-change="pageSizeChange"></a-table>
			<!-- 表格 end-->
		</a-card>
	</div>
</template>
<script lang="ts" setup>
import { computed, reactive, ref } from 'vue';
import useLoading from '@/hooks/loading';
import { BasePagination }  from '@/api/common';
import { UserEntity, UserQueryParams, queryUserList } from '@/api/pv/demo';

const position = ref<string>("day");

const customColumnsOne = [{
	title: '设备',
	dataIndex: 'device',
}, {
	title: '参数',
	dataIndex: 'params',
}, {
	title: '1天',
	dataIndex: '1',
}, {
	title: '2天',
	dataIndex: '2',
}, {
	title: '3天',
	dataIndex: '3'
}, {
	title: '4天',
	dataIndex: '4'
}, {
	title: '5天',
	dataIndex: '5'
}, {
	title: '6天',
	dataIndex: '6'
}, {
	title: '最大值',
	dataIndex: 'maxValue'
}];


const customColumnsTwo = [{
	title: '设备',
	dataIndex: 'device',
}, {
	title: '参数',
	dataIndex: 'params',
}, {
	title: '1时',
	dataIndex: '1时',
}, {
	title: '2时',
	dataIndex: '2时',
}, {
	title: '3时',
	dataIndex: '3时'
}, {
	title: '4时',
	dataIndex: '4时'
}, {
	title: '5时',
	dataIndex: '5时'
}, {
	title: '6时',
	dataIndex: '6时'
}, {
	title: '7时',
	dataIndex: '7时'
}, {
	title: '最大值',
	dataIndex: 'maxValue'
}];


const dataOne = [{
	device: '1#350KVA-1变压器-按天',
	params: "用电量",
	1: "10",
	2: "20",
	3: "30",
	4: "40",
	5: "50",
	6: "60",
	maxValue: 60
}];


const dataTwo = [{
	device: '1#350KVA-2变压器-按小时',
	params: "用电量",
	"1时": "10",
	"2时": "20",
	"3时": "30",
	"4时": "40",
	"5时": "50",
	"6时": "60",
	"7时": "70",
	maxValue: 70
}];

let customColumnsValue = customColumnsOne;
let data = dataOne;


const positionChange = (value: string | number | boolean, ev: Event) => {
	if(value == "day"){
		customColumnsValue = customColumnsOne;
		data = dataOne;
	}else{
		customColumnsValue = customColumnsTwo;
		data = dataTwo;
	}
}

//生成查询条件
const generateFormModel = () => {
	return {
		trueName: '',
		phone: '',
	};
};
const formModel = ref(generateFormModel());

//加载中
const { loading, setLoading } = useLoading(true);
//设置表格列
const columns = computed<any[]>(() => [
	{
		title: "编号",
		dataIndex: 'id',
	},
	{
		title: "昵称",
		dataIndex: 'nickName',
	},
	{
		title: "姓名",
		dataIndex: 'trueName',
	},
	{
		title: "手机号",
		dataIndex: 'phone',
	},
	{
		title: "注册时间",
		dataIndex: 'registerTime',
	},
	{
		title: "状态",
		dataIndex: 'status',
		slotName: 'status',
	},
	{
		title: "用户地址",
		dataIndex: 'address',
	},
	{
		title: "操作",
		dataIndex: 'operations',
		slotName: 'operations',
	}
]);

const timeChange = (time: any) => {
	console.log(time);
}

//表格数据
const renderData = ref<UserEntity[]>([]);
//设置表格分页
const pagination: any = reactive({ ...BasePagination()});

//查询方法
const search = () => {
	fetchData({
		...pagination,
		...formModel.value,
	} as unknown as UserQueryParams);
};

//重置查询
const reset = () => {
	formModel.value = generateFormModel();
};

//分页变化
const onPageChange = (current: number) => {
	fetchData({ ...pagination, current });
};

const pageSizeChange = (val: number) => {
	console.log('pageSizeChange', val)
	pagination.pageSize = val;
	console.log(pagination)
}

const fetchData = async (
	params: UserQueryParams = { current: 1, pageSize: 20 }
) => {
	setLoading(true);
	try {
		const { data } = await queryUserList(params);
		renderData.value = data.list;
		pagination.current = params.current;
		pagination.total = data.total;
	} catch (err) {
		// you can report use errorHandler or other
	} finally {
		setLoading(false);
	}
};

fetchData();
</script>
<style scoped lang="less">
:deep(.arco-table-th) {
	&:last-child {
		.arco-table-th-item-title {
			margin-left: 16px;
		}
	}
}

.action-icon {
	margin-left: 12px;
	cursor: pointer;
}

.active {
	color: #0960bd;
	background-color: #e3f4fc;
}

.setting {
	display: flex;
	align-items: center;
	width: 200px;

	.title {
		margin-left: 12px;
		cursor: pointer;
	}
}
</style>
