 * 该包是帧的apdu的数据结构构成的集合
 * ApplicationProtocolDataUnit--应用协议数据单元
 * APDU是104帧的数据结构
 * APDU包含APCI ASDU
 * U和S帧的APDU仅包含APCI
 * I帧的APDU包含APCI和ASDU
 * 由于APCI所含的信息量较少故将APCI概念省略
 * 故将APDU作为基础的数据结构，
 * 其中包含起始字符0x68，报文长度，控制域。
 * APDU中的ASDU==null则为U或S帧
 * ASDU为应用数据单元其中包含
 * ASDU数据单元类型，可变限定词，传输原因，源地址，应用数据单元地址，数据帧
 * 数据帧由于类型不同结构也不尽相同。
 * 详见 asdu 包