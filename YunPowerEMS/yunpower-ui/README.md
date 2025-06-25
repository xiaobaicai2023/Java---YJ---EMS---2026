# Arco 文档：https://arco.design/vue/docs/start
# Arco Design Pro

## 配置
```
1、配置Host
    # 由于不同网络对镜像的访问有限制，所以统一配置Host
    185.199.110.133 raw.githubusercontent.com

2、配置源
    npm config set registry https://registry.npmmirror.com/ -g 

3、查看
    npm config get registry

## 安装项目依赖
```
npm install
```

### 运行项目
```
npm run dev
```

### 打包发布
```
npm run build
```

### 打包发布（Demo站点）
```
npm run build:demo
```
