// import test from './alert'
window.onload = function() {
    // 配置项目地址及响应拦截器
    axios.defaults.baseURL = 'http://ali.hepengju.com:9010/data/';
    axios.defaults.headers.post['Content-Type'] = 'application/json';

    axios.interceptors.response.use(function(response) {
        // 下载文件的处理
        if (response.config.responseType == 'blob') {
            // 正常下载服务器返回的是此响应头
            if (response.headers['content-type'] == 'application/octet-stream') {
                let fileName = response.headers['original-filename-encode']
                let blob = new Blob([response.data]);
                if (window.navigator.msSaveOrOpenBlob) {
                    try {
                        window.navigator.msSaveOrOpenBlob(blob, fileName);
                    } catch (e) {} // 兼容ie11
                } else {
                    let downEle = document.createElement('a');
                    downEle.download = decodeURI(fileName);
                    downEle.href = window.URL.createObjectURL(blob);
                    downEle.click();
                    downEle.remove();
                    window.URL.revokeObjectURL(blob);
                }
            } else {
                // 下载过程中发生错误, 返回的响应头是 'application/json'
                let errBlob = new Blob([resp.data], {type: 'application/json'});
                let fr = new FileReader();
                fr.readAsText(errBlob);
                fr.onload = function() {
                    let jsonR = JSON.parse(this.result);
                    showModal(jsonR.errMsg)
                    // alert(jsonR.errMsg)
                }
            }
        }

        // 出现错误的处理
        if (response.headers['content-type'] == 'application/json' && response.data.errCode != 0) {
            console.log(this)
            showModal(response.data.errMsg);
            // alert(response.data.errMsg)

        }
        return response
    }, function(error) {
        showModal("服务器连接失败！")
        // alert("服务器连接失败");
        return Promise.reject(error);
    });


    // VUE对象
    new Vue({
        el: '#app',
        data () {
            return {
                showTable:true,
                dataTooltip:'',
                btnList:[
                    // {label: '保存',       type:'info'},
                    {label: '刷新',       type:'primary'},
                    {label: '删除全部',   type:'warning'},
                ],
                downloadMode:[
                    'csv',
                    'tsv',
                    'sql',
                    'excel'
                ],
                configForm:{
                    columnTitle:'',
                    min: '',
                    max: '',
                    code: '',
                    format: '',
                    codeMulti: false,
                    prefix:'',
                    suffix:'',
                },
                downloadNumber:1000,
                fileName:'用户表',   // 文件名
                tableName:'z010_user',  //表名
                columns: [],
                data: [],
                dataTmp:[],
                dataList:[],
                selectColumn:[], //选中的列
                currentColumn:[], // 当前列
                metaList: [],  //  json数组
                currentIndex:'', //当前的下标
                count:1 , //   key+count 列的唯一键
            }
        },
        methods: {
            btnClick(event) {
                let val = event.target.innerText;
                switch (val) {
                    // case '保存':
                    //     break;
                    case '刷新':
                        this.refresh();
                        break;
                    case '删除全部':
                        this.deleteAll();
                        break;
                }
            },

            //蒙层指引
            guide(){
                introJs().setOptions({
                    prevLabel:"上一步",
                    nextLabel:"下一步",
                    skipLabel:"跳过",
                    doneLabel:"结束",
                    exitOnOverlayClick: false,// 是否允许点击空白处退出，默认true：允许
                    showStepNumbers: true,// 是否显示说明的数据步骤  默认true:显示
                    //对应的数组，顺序出现每一步引导提示
                    steps: [
                        {
                            //第一步引导
                            //这个属性类似于jquery的选择器， 可以通过jquery选择器的方式来选择你需要选中的对象进行指引
                            element: '#title_inner',
                            //这里是每个引导框具体的文字内容，中间可以编写HTML代码
                            intro: '选择所需生成器',
                            //这里可以规定引导框相对于选中对象出现的位置 top,bottom,left,right
                            position: 'bottom',
                        },
                        {
                            //第二步引导
                            element: '#title_config',
                            intro: '点击生成器表头可进行详细配置',
                            position: 'bottom'
                        },
                        {
                            //第二步引导
                            element: '#download',
                            intro: '设置下载行数、文件名、表名后，可进行下载',
                            position: 'bottom'
                        }
                    ]
                }).start()
            },

            // 样例数据展示
            mouseFn(item){
                this.dataTooltip = item.sampleData
            },

            refresh() {
                console.log(this.columns);
                this.getCurrentColumns(this.columns, 'refresh');
                console.log(this.metaList);
                let params = {'metaList': this.metaList};
                params = JSON.stringify(params);
                axios.post('refreshTable',params).then(res=>{
                    let newData = res.data.data;
                    for (let j = 0; j < newData.length ; j++) {
                        for (let i = 0; i < this.metaList.length; i++) {
                            let newKey = this.metaList[i].columnKey;
                            this.$set(this.data[j],newKey,newData[j][newKey])
                        }
                    }
                    // console.log(this.data);
                })
            },

            // 显示配置
            showConfig(item) {
                this.currentColumn = [];
                this.currentColumn.push(item.column);
                this.selectColumn = [];
                this.$set(item.column,['className'],'demo-table-info-column');
                for (let key in this.configForm){
                    this.configForm[key] = item.column[key];
                    this.configForm.columnTitle = item.column.title;
                }
                this.selectColumn.push(item.column);
                this.currentIndex = item.index;
            },

            // 保存配置
            save() {
                if (this.columns.length == 0) {
                    this.$Message.warning({
                        content:'暂无生成器可保存，请选择所需生成器！'
                    });
                    return
                }
                if (this.selectColumn.length == 0) {
                    this.$Message.warning({
                        content:'请选择需要详细配置的生成器！'
                    });
                    return
                }
                this.count++;
                for (let key in this.configForm){
                    this.columns[this.currentIndex][key] =this.configForm[key] ;
                }
                this.columns[this.currentIndex].title = this.configForm.columnTitle;
                // this.columns[this.currentIndex].key =  this.columns[this.currentIndex].key + this.count;
                let nameStr = this.columns[this.currentIndex].key.match(/[\u4e00-\u9fa5]{2,}/g).toString(); //用正则把文字匹配出来
                this.columns[this.currentIndex].key = nameStr  + this.count;
                this.selectColumn = [];
                this.selectColumn.push(this.columns[this.currentIndex]);
                this.singleRestore(this.selectColumn[0])
            },

            // 刷新单列
            singleRestore(val) {
                let otherObj = {
                    columnKey: val.key,
                    columnName:val.name,
                    name: val.name,
                    type: val.type,
                };
                let metaObj = Object.assign({},otherObj,this.configForm);
                this.metaList.push(metaObj);
                let params = JSON.stringify({'metaList':this.metaList});
                axios.post('refreshTable',params)
                    .then(res => {
                        this.metaList = [];
                        let newData = res.data.data;
                        let newkey =  val.key;
                        for (let i = 0; i < newData.length ; i++) {
                            this.$set(this.data[i],newkey,newData[i][newkey])
                        }
                    });
                console.log(this.columns);
                this.$set(this.selectColumn[0],['className'],'demo-table-info-column');
            },

            // 还原配置
            restore() {
                if (this.columns.length == 0) {
                    this.$Message.warning({
                        content:'暂无生成器可还原，请选择所需生成器！'
                    });
                    return
                }
                if (this.selectColumn.length == 0) {
                    this.$Message.warning({
                        content:'请选择需要还原的生成器！'
                    });
                    return
                }
                for (let item in this.configForm){
                    this.configForm[item] = this.currentColumn[0][item]
                }
                this.configForm.columnTitle = this.currentColumn[0].title;
                this.columns[this.currentIndex].title = this.currentColumn[0].title;
                this.columns[this.currentIndex].key =  this.currentColumn[0].key ;
                console.log(this.currentColumn);
                this.singleRestore(this.currentColumn[0])
            },

            // 点击各个生成器生成样例数据
            genClick(event){
                this.showTable = false;
                let name = event.target.innerText;
                let dataArr = this.dataList.date_number.concat(this.dataList.string, this.dataList.custom);
                dataArr.forEach(item => {
                    if(name == item.columnTitle) {
                        this.columns.push({
                            title: name,
                            key: name,
                            renderHeader:(h,params) => {
                                return h('div',[
                                    h('span',{
                                        style:{
                                            cursor:'pointer',
                                        },
                                        on: {
                                            click: ()=>{
                                                this.showConfig(params)
                                            },
                                        }
                                    }, params.column.title),
                                    h('icon',{
                                        props: {
                                            type: 'ios-close',
                                            size: '18',
                                        },
                                        style:{
                                            cursor:'pointer'
                                        },
                                        on: {
                                            click: () => {
                                                this.deleteCol(params)
                                            },
                                        }
                                    })
                                ])
                            },
                            min: item.min,
                            max: item.max,
                            code: item.code,
                            codeMulti: item.codeMulti,
                            format:item.format,
                            prefix:item.prefix,
                            suffix:item.suffix,
                            name: item.name,
                            type:item.type,
                            ellipsis:true,
                            tooltip:true,
                            minWidth:100,
                        });
                    }
                });

            },

            // 删除列
            deleteCol(params) {
                this.columns.splice(params.index,1);
                this.count = 0;
                if (this.columns.length == 0){
                    this.showTable = true
                }
            },

            //删除全部列
            deleteAll() {
                if (this.columns.length == 0) {
                    this.$Message.warning({
                        content:'暂无生成器可删除，请选择所需生成器！'
                    });
                    return
                }
                this.$Modal.confirm({
                    title: `系统提示`,
                    content: `确认删除全部列吗？`,
                    onOk: ()=>{
                        this.columns = [];
                        this.showTable = true;
                    },
                    onCancel: ()=>{
                    }
                });
            },

            // 生成 dataTmp 空对象
            makeData () {
                for (let i = 0 ; i < 10 ; i++) {
                    let obj = {};
                    this.dataTmp.push(obj)
                }
            },

            //封装各个模板数据
            packageData() {
                // 数字日期生成器
                for (let i = 0 ; i < this.dataList.date_number.length; i++) {
                    // 向dataTmp 10条对象里添加属性名值对
                    for (let j = 0 ; j < this.dataList.date_number[i].sampleData.length; j++) {
                        this.dataTmp[j][this.dataList.date_number[i].columnTitle] = this.dataList.date_number[i].sampleData[j];
                    }
                }
                // 字符生成器
                for (let i = 0 ; i < this.dataList.string.length ; i++) {
                    for (let j = 0 ; j < this.dataList.string[i].sampleData.length; j++) {
                        this.dataTmp[j][this.dataList.string[i].columnTitle] = this.dataList.string[i].sampleData[j]
                    }
                }
                // 定制生成器
                for (let i = 0 ; i < this.dataList.custom.length ; i++) {
                    for (let j = 0 ; j < this.dataList.custom[i].sampleData.length; j++) {
                        this.dataTmp[j][this.dataList.custom[i].columnTitle] = this.dataList.custom[i].sampleData[j]
                    }
                }
                this.data = JSON.parse(JSON.stringify(this.dataTmp));
                console.log(this.data);
            },

            // 获取当前表格列信息
            getCurrentColumns(val, event) {
                if (event == 'downLoad') {
                    this.metaList = [];
                    val.forEach(item => {
                        this.metaList.push({
                            name: item.name,
                            min: item.min,
                            max: item.max,
                            code: item.code,
                            codeMulti: item.codeMulti,
                            format:item.format,
                            prefix:item.prefix,
                            suffix:item.suffix,
                            columnKey: item.key,
                            columnName:item.name,
                            columnTitle:item.title,
                            type:item.type,
                        });
                    });
                } else {
                    this.count++;
                    this.metaList = [];
                    val.forEach(item => {
                        item.key = item.key.match(/[\u4e00-\u9fa5]{2,}/g).toString() + this.count;
                        this.metaList.push({
                            name: item.name,
                            min: item.min,
                            max: item.max,
                            code: item.code,
                            codeMulti: item.codeMulti,
                            format:item.format,
                            prefix:item.prefix,
                            suffix:item.suffix,
                            columnKey: item.key,
                            columnName:item.name,
                            columnTitle:item.title,
                            type:item.type,
                        });
                    });
                }
            },

            // 下载表格
            downloadFile(val) {
                if (this.columns.length == 0) {
                    this.$Message.warning({
                        content:'暂无数据可下载，请先选择所需生成器生成数据！'
                    });
                    return
                }
                this.getCurrentColumns(this.columns, 'downLoad');
                let params = {'fileFormat':val,
                    'sampleSize':this.downloadNumber,
                    'metaList':this.metaList,
                    'tableName':this.tableName,
                    'fileName': this.fileName};
                params = JSON.stringify(params);
                axios.post('downTable',params, {
                    responseType: 'blob'
                }).then((res)=>{
                    console.log(this.columns);
                    console.log(this.data);
                })
            },

            //获取所有数据
            getData() {
                axios.get('getGenMap')
                    .then(res => {
                        this.dataList = res.data.data;
                        console.log(this.dataList);
                        this.packageData();
                    })
                    .catch(function (error) {
                        console.log(error);
                    })
            },

            showModal(val){
                this.$Modal.warning({
                    title: `系统提示`,
                    content: val,
                });
            }
        },
        mounted: function () {
            this.makeData();
            this.getData();
            window.showModal = this.showModal  //把VUE实例的方法赋给window
        },
    })
};
