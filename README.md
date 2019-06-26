# ont-sourcing

## 接口

* [创建dnaid](#创建dnaid)
* [获取access_token](#获取access_token)

* [存证](#存证)
* [批量存证](#批量存证)

* [根据hash取证](#根据hash取证)
* [根据hash删除存证](#根据hash删除存证)

* [获取存证总条数](#获取存证总条数)
* [获取存证历史记录](#获取存证历史记录)

* [浏览器存证历史记录](#浏览器存证历史记录)
* [浏览器根据hash取证](#浏览器根据hash取证)

### 附
* [错误码](#错误码)

## 接口规范

### 创建dnaid

注：该dnaid并未在链上注册.

```text
url：/api/v1/dnaid/create
method：POST
```

- 请求：

```json
{
    "username":"entity1",
    "password":"888888"
}
```

| Field_Name | Type   | Description |
|:-----------|:-------|:------------|
| username   | String | 设置用户名    |
| password   | String | 设置密码    |

- 响应：

```json
{
    "desc": "SUCCESS",
    "action": "creatednaid",
    "result": "did:dna:APd46pRkKCSzF9tSqrX8kBDbaDxufAgwVw",
    "error": 0,
    "version": "1.0.0"
}
```

| Field_Name | Type   | Description                   |
|:-----------|:-------|:------------------------------|
| error      | int    | 错误码                        |
| action     | String | 动作标志                      |
| desc       | String | 成功返回SUCCESS，失败返回错误描述 |
| result     | String | 成功返回dnaid，失败返回""     |
| version    | String | 版本号                        |（）


### 获取access_token

```text
url：/api/v1/dnaid/token
method：POST
```

- 请求：

```json
{
    "user_ontid":"did:dna:APd46pRkKCSzF9tSqrX8kBDbaDxufAgwVw"
}
```

| Field_Name | Type   | Description |
|:-----------|:-------|:------------|
| username   | String | 设置用户名    |
| password   | String | 设置密码    |

- 响应：

```json
{
    "result": {
        "access_token": "eyJ0eXAiOiJKV1QiLCJhbGciOiJFUzI1NiJ9.eyJhdWQiOiJkaWQ6ZG5hOkFQZDQ2cFJrS0NTekY5dFNxclg4a0JEYmFEeHVmQWd3VnciLCJpc3MiOiJkaWQ6b250OkFkajdXNVoyaFRlS0g3WXdKc2ZNekx1d2lENjcxbXZKNlgiLCJleHAiOjE1NjE2MDY1MTAsImlhdCI6MTU2MTUyMDExMCwianRpIjoiOTE0Nzg0OWI3NjkwNGNlY2JmYmE5YmRkZmU3ZThkNTAiLCJjb250ZW50Ijp7InR5cGUiOiJhY2Nlc3NfdG9rZW4iLCJvbnRpZCI6ImRpZDpkbmE6QVBkNDZwUmtLQ1N6Rjl0U3FyWDhrQkRiYUR4dWZBZ3dWdyJ9fQ.MDE3YjZlNzI0ZDY0ZjMyMGRlZjgxNTZmZmJhMzQ5NTVlNWI4NjcyNmExOGZmNzRkZWE2ZDYwZDRjNTU2NGMzZjNmYzZhNmI4NmRhYzE4OTI2YzU1MWI4NzA3Y2RiZGU0OTQ2ZDE1NjE0Yjc0MGU5ZTE1ODkwZmU4YzQyYzA2YTVhYw",
        "user_ontid": "did:dna:APd46pRkKCSzF9tSqrX8kBDbaDxufAgwVw"
    },
    "error": 0,
    "action": "getAccessToken",
    "version": "1.0.0",
    "desc": "SUCCESS"
}
```

| Field_Name | Type   | Description                   |
|:-----------|:-------|:------------------------------|
| error      | int    | 错误码                        |
| action     | String | 动作标志                      |
| desc       | String | 成功返回SUCCESS，失败返回错误描述 |
| result     | String | 成功返回结果，失败返回""     |
| version    | String | 版本号                        |



### 存证

```text
url：/api/v1/attestation/put
method：POST
```

- 请求：

```json
{
	"access_token":"eyJ0eXAiOiJKV1QiLCJhbGciOiJFUzI1NiJ9.eyJhdWQiOiJkaWQ6b250OkFVRG11NEoyVzF2cUpIRHRMUDhVeEhhdWoyZUtzUUh4dTYiLCJpc3MiOiJkaWQ6b250OkFhdlJRcVhlOVByYVY1dFlnQnF2VjRiVXE4TFNzdmpjV1MiLCJleHAiOjE1NTcyODM2MTAsImlhdCI6MTU1NzE5NzIxMCwianRpIjoiYmQ5NTZhNGI1YzYxNGYxN2I2YTgxNDkyZDI5NDIyYTQiLCJjb250ZW50Ijp7InR5cGUiOiJhY2Nlc3NfdG9rZW4iLCJvbnRpZCI6ImRpZDpvbnQ6QWExWFBhcEpIR0dqSFF0TjJIZHliOUFQdjdIZmlZeHRSeiJ9fQ.MDFiMjFkMjg5OGJmYjZlZGQzMmM5ZjY0ZWIxMDA0OGYxNGNkOGE2MTBhYTZmZGNiZTg4OWQyNzI0MjMwZDVjMjk3Y2Q3ZDhjMzlhOGYzZDJkYjE1YzFhMTcxM2Y3OTU4ZjkzYzRjOGI2NmU2ODM5YmFhNjE4NWRjMTlkZjU3YThkYQ",
	"user_dnaid":"",
	"filehash":"111175b25e49f2767522d332057c3e6bb1144c842dce47913dc8222927999999",
	"metadata": {
            "name":"",
            "title": "",
            "tags": "",
            "description":"",
            "timestamp": "",
            "location":""
	},
	"context": {
	    "image": ["",""],
	    "text": ["",""]
	},
	"signature":"",
	"type": "TEXT",
	"async": true
}
```

| Field_Name | Type   | Description |
|:-----------|:-------|:------------|
| access_token   | String | access_token    |
| user_dnaid   | String | 空表示自己存证，否则表示被存证    |
| filehash   | String | 文件hash    |
| metadata   | JSON |     |
| context   | JSON |     |
| signature   | String |     |
| type   | String | PDF/TEXT/IMAGE/VIDEO   |
| async   | boolean |  true表示异步，false表示同步，默认为false   |

- 响应：

当 async 为 true 时：
```json
{
    "result": true,
    "error": 0,
    "action": "putAttestation",
    "desc": "SUCCESS",
    "version": "1.0.0"
}
```

当 async 为 false 时：
```json
{
    "version": "1.0.0",
    "desc": "SUCCESS",
    "result": {
        "txhash": "d6459de184af36ccbc786e19f30ea14961f29b85aa330ea58e07463a73532bac"
    },
    "error": 0,
    "action": "putAttestation"
}
```

| Field_Name | Type   | Description                   |
|:-----------|:-------|:------------------------------|
| result     |  |    |
| error      | int    | 错误码                        |
| action     | String | 动作标志                      |
| desc       | String | 成功返回SUCCESS，失败返回错误描述 |
| version    | String | 版本号                        |


### 批量存证

```text
url：/api/v1/attestations/put
method：POST
```

- 请求：

```json
{
    "access_token": "eyJ0eXAiOiJKV1QiLCJhbGciOiJFUzI1NiJ9.eyJhdWQiOiJkaWQ6b250OkFVRG11NEoyVzF2cUpIRHRMUDhVeEhhdWoyZUtzUUh4dTYiLCJpc3MiOiJkaWQ6b250OkFhdlJRcVhlOVByYVY1dFlnQnF2VjRiVXE4TFNzdmpjV1MiLCJleHAiOjE1NTc4OTM4MzYsImlhdCI6MTU1NzgwNzQzNiwianRpIjoiNjM3YzY4ODQxMzc1NGMxMGE1ZDM3NDY0MTcwMWQwMTIiLCJjb250ZW50Ijp7InR5cGUiOiJhY2Nlc3NfdG9rZW4iLCJvbnRpZCI6ImRpZDpvbnQ6QWExWFBhcEpIR0dqSFF0TjJIZHliOUFQdjdIZmlZeHRSeiJ9fQ.MDE1YmFjYTI0MTI3ODI2YmI0OWI5YzY1YjU4YTg1Njk5NmRkNjlmMTc1MTM3MGIwM2NhOTQ0ZTY4YzI2NzRjMWU2M2U1MTQ2ODZkYTE3ZWU4OGE2N2E4ZTE1MDQ4ODQzNDZiOTYxMGI4MjhjMzhmNGFkMGNiYTY4MDBhZDVjNDZhNw",
    "user_dnaid": "",
    "async": true,
    "filelist": [
        {
            "filehash":"111175b25e49f2767522d332057c3e6bb1144c842dce47913dc8222927999999",
            "metadata": {
                    "name":"",
                    "title": "",
                    "tags": "",
                    "description":"",
                    "timestamp": "",
                    "location":""
            },
            "context": {
                "image": ["",""],
                "text": ["",""]
            },
            "signature":"",
            "type": "TEXT"
        },
        {
            "filehash":"111175b25e49f2767522d332057c3e6bb1144c842dce47913dc8222927999999",
            "metadata": {
                     "name":"",
                     "title": "",
                     "tags": "",
                     "description":"",
                     "timestamp": "",
                     "location":""
            },
            "context": {
                "image": ["",""],
                "text": ["",""]
            },
            "signature":"",
            "type": "TEXT"
        }
    ]
}
```

| Field_Name | Type   | Description |
|:-----------|:-------|:------------|
| access_token   | String | access_token|
| user_dnaid   | String | 空表示自己存证，否则表示被存证    |
| filelist   | String | 批量文件(总数不能超过30条)    |
| async   | boolean |  true表示异步，false表示同步，默认为false   |


- 响应：

当 async 为 true 时：
```json
{
    "result": true,
    "error": 0,
    "action": "putAttestations",
    "desc": "SUCCESS",
    "version": "1.0.0"
}
```


当 async 为 false 时：
```json
{
    "result": [
        {
            "txhash": "58eb1b7414d51988899dbaf54ff891aca55eb7ba2f4a5b5008af18a874187d02"
        },
        {
            "txhash": "6cada409428b2d7f4e2186a2a95cd0bdbe82e643ff697b4077129438da0b2e9b"
        }
    ],
    "error": 0,
    "action": "putAttestations",
    "desc": "SUCCESS",
    "version": "1.0.0"
}
```


| Field_Name | Type   | Description                   |
|:-----------|:-------|:------------------------------|
| error      | int    | 错误码                        |
| action     | String | 动作标志                      |
| desc       | String | 成功返回SUCCESS，失败返回错误描述 |
| result     |  |    |
| version    | String | 版本号                        |


### 根据hash取证

```text
url：/api/v1/attestation/hash
method：POST
```

- 请求：

```json
{
	"access_token": "eyJ0eXAiOiJKV1QiLCJhbGciOiJFUzI1NiJ9.eyJhdWQiOiJkaWQ6b250OkFNdmpVV1o2Y25BQVVzQk43dWpBQnRMUzlHbWVoOFNQU2oiLCJpc3MiOiJkaWQ6b250OkFhdlJRcVhlOVByYVY1dFlnQnF2VjRiVXE4TFNzdmpjV1MiLCJleHAiOjE1NTUwNTU3MzksImlhdCI6MTU1NDk2OTMzOSwianRpIjoiZjQ1ZmMyMmVkMjBhNDFhMGE1YzdhMzZhYjIxZTkxNTAiLCJjb250ZW50Ijp7InR5cGUiOiJhY2Nlc3NfdG9rZW4iLCJvbnRpZCI6ImRpZDpvbnQ6QU14clNHSHl4Z25XUzZxYzFRalROWWVFYXczWDNEdnpoZiJ9fQ.MDFiZDVhYWQ2MzRkNzlkOTU3ZjE3YWYyNDc3MDUyZGUxNzJjYjdmYjgxZWViOThmYTg2ODgyM2ZiYjM5ZjIyMjZiYWZlYTlkNGFkNjMwMzM0OWY4N2YyYzBiZDlmNzg5M2IzYjhiYjdkZTg1MjFmYzQ1MDMwOGY2NGRmM2E5ZjkwNg",
	"hash":"e81475b25e49f2767522d332057c3e6bb1144c842dce47913dc8222927102c67"
}
```

| Field_Name | Type   | Description |
|:-----------|:-------|:------------|
| access_token   | String | access_token    |
| hash   | String | 文件hash或者交易hash   |

- 响应：

```json
{
    "result": [
        {
            "dnaid": "did:dna:Aa1XPapJHGGjHQtN2Hdyb9APv7HfiYxtRz",
            "companyDnaid": "did:dna:Aa1XPapJHGGjHQtN2Hdyb9APv7HfiYxtRz",
            "detail": "some message about the file ...",
            "type": "TEXT",
            "timestamp": "2019-04-22T07:32:57.000+0000",
            "timestampSign": "950ef......",
            "filehash": "111175b25e49f2767522d332057c3e6bb1144c842dce47913dc8222927999999",
            "txhash": "ee973d13c6ed2d8c7391223b4fb6f5c785f402d81d41b02ab7590113cbb00752",
            "createTime": "2019-04-22T07:32:57.000+0000",
            "updateTime": null,
            "height": 1621684
        },
        {
            "dnaid": "did:dna:Aa1XPapJHGGjHQtN2Hdyb9APv7HfiY1234",
            "companyDnaid": "did:dna:Aa1XPapJHGGjHQtN2Hdyb9APv7HfiYxtRz",
            "detail": "some message about the file ...",
            "type": "TEXT",
            "timestamp": "2019-04-22T07:32:24.000+0000",
            "timestampSign": "960ef......",
            "filehash": "111175b25e49f2767522d332057c3e6bb1144c842dce47913dc8222927999999",
            "txhash": "1ab4b5b2c6c89b4f1a553b7aef30c3f3ef203a323d23cd383261cc6d0df73870",
            "createTime": "2019-04-22T07:32:25.000+0000",
            "updateTime": null,
            "height": 1621682
        },
        {
            "dnaid": "did:dna:Aa1XPapJHGGjHQtN2Hdyb9APv7HfiYxtRz",
            "companyDnaid": "",
            "detail": "some message about the file ...",
            "type": "TEXT",
            "timestamp": "2019-04-22T04:22:55.000+0000",
            "timestampSign": "950ef......",
            "filehash": "111175b25e49f2767522d332057c3e6bb1144c842dce47913dc8222927999999",
            "txhash": "0437084a4f6204aad88fa1507fc13a44f83cecf44fc925692f9bc43f23e52fc3",
            "createTime": "2019-04-22T04:22:57.000+0000",
            "updateTime": null,
            "height": 1621275
        }
    ],
    "error": 0,
    "desc": "SUCCESS",
    "action": "selectByDnaidAndHash",
    "version": "1.0.0"
}
```

| Field_Name | Type   | Description                   |
|:-----------|:-------|:------------------------------|
| error      | int    | 错误码                        |
| action     | String | 动作标志                      |
| desc       | String | 成功返回SUCCESS，失败返回错误描述 |
| result     | String | 成功返回存证记录，失败返回""     |
| version    | String | 版本号                        |


### 根据hash删除存证

```text
url：/api/v1/attestation/hash/delete
method：POST
```

- 请求：

```json
{
	"access_token": "eyJ0eXAiOiJKV1QiLCJhbGciOiJFUzI1NiJ9.eyJhdWQiOiJkaWQ6b250OkFNdmpVV1o2Y25BQVVzQk43dWpBQnRMUzlHbWVoOFNQU2oiLCJpc3MiOiJkaWQ6b250OkFhdlJRcVhlOVByYVY1dFlnQnF2VjRiVXE4TFNzdmpjV1MiLCJleHAiOjE1NTUwNTU3MzksImlhdCI6MTU1NDk2OTMzOSwianRpIjoiZjQ1ZmMyMmVkMjBhNDFhMGE1YzdhMzZhYjIxZTkxNTAiLCJjb250ZW50Ijp7InR5cGUiOiJhY2Nlc3NfdG9rZW4iLCJvbnRpZCI6ImRpZDpvbnQ6QU14clNHSHl4Z25XUzZxYzFRalROWWVFYXczWDNEdnpoZiJ9fQ.MDFiZDVhYWQ2MzRkNzlkOTU3ZjE3YWYyNDc3MDUyZGUxNzJjYjdmYjgxZWViOThmYTg2ODgyM2ZiYjM5ZjIyMjZiYWZlYTlkNGFkNjMwMzM0OWY4N2YyYzBiZDlmNzg5M2IzYjhiYjdkZTg1MjFmYzQ1MDMwOGY2NGRmM2E5ZjkwNg",
	"hash":"e81475b25e49f2767522d332057c3e6bb1144c842dce47913dc8222927102c67"
}
```

| Field_Name | Type   | Description |
|:-----------|:-------|:------------|
| access_token   | String | access_token    |
| hash   | String | 交易hash   |

- 响应：

```json
{
    "version": "1.0.0",
    "error": 0,
    "action": "deleteByDnaidAndHash",
    "result": true,
    "desc": "SUCCESS"
}
```

| Field_Name | Type   | Description                   |
|:-----------|:-------|:------------------------------|
| error      | int    | 错误码                        |
| action     | String | 动作标志                      |
| desc       | String | 成功返回SUCCESS，失败返回错误描述 |
| result     | String | 成功返回true     |
| version    | String | 版本号                        |



### 获取存证总条数

```text
url：/api/v1/attestation/count
method：POST
```

- 请求：

```json
{
		"access_token": "eyJ0eXAiOiJKV1QiLCJhbGciOiJFUzI1NiJ9.eyJhdWQiOiJkaWQ6b250OkFNdmpVV1o2Y25BQVVzQk43dWpBQnRMUzlHbWVoOFNQU2oiLCJpc3MiOiJkaWQ6b250OkFhdlJRcVhlOVByYVY1dFlnQnF2VjRiVXE4TFNzdmpjV1MiLCJleHAiOjE1NTUwNTU3MzksImlhdCI6MTU1NDk2OTMzOSwianRpIjoiZjQ1ZmMyMmVkMjBhNDFhMGE1YzdhMzZhYjIxZTkxNTAiLCJjb250ZW50Ijp7InR5cGUiOiJhY2Nlc3NfdG9rZW4iLCJvbnRpZCI6ImRpZDpvbnQ6QU14clNHSHl4Z25XUzZxYzFRalROWWVFYXczWDNEdnpoZiJ9fQ.MDFiZDVhYWQ2MzRkNzlkOTU3ZjE3YWYyNDc3MDUyZGUxNzJjYjdmYjgxZWViOThmYTg2ODgyM2ZiYjM5ZjIyMjZiYWZlYTlkNGFkNjMwMzM0OWY4N2YyYzBiZDlmNzg5M2IzYjhiYjdkZTg1MjFmYzQ1MDMwOGY2NGRmM2E5ZjkwNg"
}
```

| Field_Name | Type   | Description |
|:-----------|:-------|:------------|
| dnaid   | String | 需要查询的dnaid    |
| access_token   | String | access_token    |



- 响应：

```json
{
    "result": 6,
    "error": 0,
    "desc": "SUCCESS",
    "action": "count",
    "version": "1.0.0"
}
```

| Field_Name | Type   | Description                   |
|:-----------|:-------|:------------------------------|
| error      | int    | 错误码                        |
| action     | String | 动作标志                      |
| desc       | String | 成功返回SUCCESS，失败返回错误描述 |
| result     | String | 成功返回总条数，失败返回""
| version    | String | 版本号                     


### 获取存证历史记录

```text
url：/api/v1/attestation/history
method：POST
```

- 请求：

```json
{
	"access_token": "eyJ0eXAiOiJKV1QiLCJhbGciOiJFUzI1NiJ9.eyJhdWQiOiJkaWQ6b250OkFNdmpVV1o2Y25BQVVzQk43dWpBQnRMUzlHbWVoOFNQU2oiLCJpc3MiOiJkaWQ6b250OkFhdlJRcVhlOVByYVY1dFlnQnF2VjRiVXE4TFNzdmpjV1MiLCJleHAiOjE1NTU5OTMzNjAsImlhdCI6MTU1NTkwNjk2MCwianRpIjoiMTYwY2FkNjNmZTdkNGY5MTk3NGFjZjQzYWNlMzkzNmYiLCJjb250ZW50Ijp7InR5cGUiOiJhY2Nlc3NfdG9rZW4iLCJvbnRpZCI6ImRpZDpvbnQ6QWExWFBhcEpIR0dqSFF0TjJIZHliOUFQdjdIZmlZeHRSeiJ9fQ.MDE5MzE3ODk4ODU2MGQ5NGQ3MTBmZTc2Mzg1ZWE0OWRiMmRjZjczZmU2NjAyYjU0NjI2YWE0MmJmZWYwYTFkYTE0ODI5YWVmYTJjNjNlMTA5N2Y2ZjM0YTJlMTJmOGYwNWNmYzRhZWI3NzlkOWEwMWY2NDY1Y2VjYWM1MzNjYjk5Ng",
	"pageNum": 1,
	"pageSize": 3,
	"type":"TEXT"
}
```

| Field_Name | Type   | Description |
|:-----------|:-------|:------------|
| access_token   | String | access_token    |
| pageNum   | Integer | 页数，例如：1表示第1页   |
| pageSize   | Integer | 每页记录数，例如：3表示每一页3条记录。该值必须小于10。    |
| type   | String | INDEX/TEXT/IMAGE/VIDEO   |


- 响应：

```json
{
    "result": [
        {
            "dnaid": "did:dna:Aa1XPapJHGGjHQtN2Hdyb9APv7HfiY7890",
            "companyDnaid": "did:dna:Aa1XPapJHGGjHQtN2Hdyb9APv7HfiYxtRz",
            "detail": "[{\"name\":\"img1\",\"hash\":\"e81475b25e49f2767522d332057c3e6bb1144c842dce47913dc8222927888888\",\"message\":\"\"},{\"name\":\"img2\",\"hash\":\"e81475b25e49f2767522d332057c3e6bb1144c842dce47913dc8222927999999\",\"message\":\"\"},{\"name\":\"img3\",\"hash\":\"e81475b25e49f2767522d332057c3e6bb1144c842dce47913dc8222927000000\",\"message\":\"\"}]",
            "type": "INDEX",
            "timestamp": "2019-04-22T07:50:45.000+0000",
            "timestampSign": "950ef......",
            "filehash": "e81475b25e49f2767522d332057c3e6bb1144c842dce47913dc8222927777777",
            "txhash": "261944dfe5f5e83cac5d9b4c1065f508d7750e66adec85d12cb7415ef5cc1d3a",
            "createTime": "2019-04-22T07:50:46.000+0000",
            "updateTime": null,
            "height": 1621724
        },
        {
            "dnaid": "did:dna:Aa1XPapJHGGjHQtN2Hdyb9APv7HfiY7890",
            "companyDnaid": "did:dna:Aa1XPapJHGGjHQtN2Hdyb9APv7HfiYxtRz",
            "detail": "null",
            "type": "INDEX",
            "timestamp": "2019-04-22T07:47:22.000+0000",
            "timestampSign": "950ef......",
            "filehash": "e81475b25e49f2767522d332057c3e6bb1144c842dce47913dc8222927777777",
            "txhash": "99c240e7860a6016dd38de4d5fc73ea6ed533c76007a4b314741259b54f0937a",
            "createTime": "2019-04-22T07:47:23.000+0000",
            "updateTime": null,
            "height": 1621714
        },
        {
            "dnaid": "did:dna:Aa1XPapJHGGjHQtN2Hdyb9APv7HfiY7890",
            "companyDnaid": "did:dna:Aa1XPapJHGGjHQtN2Hdyb9APv7HfiYxtRz",
            "detail": "null",
            "type": "INDEX",
            "timestamp": "2019-04-22T07:42:54.000+0000",
            "timestampSign": "960ef......",
            "filehash": "e81475b25e49f2767522d332057c3e6bb1144c842dce47913dc8222927777777",
            "txhash": "4a70100fa6c5ff6b2b5484493c2c147861772821c50df27db28b01b13bc3a593",
            "createTime": "2019-04-22T07:42:55.000+0000",
            "updateTime": null,
            "height": 1621704
        }
    ],
    "error": 0,
    "desc": "SUCCESS",
    "action": "getExplorerHistory",
    "version": "1.0.0"
}
```

| Field_Name | Type   | Description                   |
|:-----------|:-------|:------------------------------|
| error      | int    | 错误码                        |
| action     | String | 动作标志                      |
| desc       | String | 成功返回SUCCESS，失败返回错误描述 |
| result     | String | 成功返回记录，失败返回""
| version    | String | 版本号                     


### 浏览器存证历史记录

```text
url：/api/v1/attestation/explorer
method：POST
```

- 请求：

```json
{
    "pageNum": 1,
    "pageSize": 3
}
```

| Field_Name | Type   | Description |
|:-----------|:-------|:------------|
| pageNum   | Integer | 页数，例如：1表示第1页   |
| pageSize   | Integer | 每页记录数，例如：3表示每一页3条记录。该值必须小于10。    |

- 响应：

```json
{
    "result": [
        {
            "dnaid": "did:dna:Aa1XPapJHGGjHQtN2Hdyb9APv7HfiYxtRz",
            "companyDnaid": "did:dna:Aa1XPapJHGGjHQtN2Hdyb9APv7HfiYxtRz",
            "detail": "some message about the file ...",
            "type": "TEXT",
            "timestamp": "2019-04-24T09:22:35.000+0000",
            "timestampSign": "960ef0...",
            "filehash": "111175b25e49f2767522d332057c3e6bb1144c842dce47913dc8222927999999",
            "txhash": "7116936d9992ce4cbb7f3531d4e3e1807201cc6163d9d5958e375f27d604889a",
            "createTime": "2019-04-24T09:22:35.000+0000",
            "updateTime": null,
            "height": 1628962
        },
        {
            "dnaid": "did:dna:Aa1XPapJHGGjHQtN2Hdyb9APv7HfiY7890",
            "companyDnaid": "did:dna:Aa1XPapJHGGjHQtN2Hdyb9APv7HfiYxtRz",
            "detail": "[{\"url\":\"http://....\"}]",
            "type": "IMAGE",
            "timestamp": "2019-04-23T03:49:20.000+0000",
            "timestampSign": "960ef0...",
            "filehash": "e81475b25e49f2767522d332057c3e6bb1144c842dce47913dc8222927000000",
            "txhash": "ef12912c68d20812acb08a0ab6a0809c85f95ebef1bd8315a3e5f891e3d12688",
            "createTime": "2019-04-23T03:49:22.000+0000",
            "updateTime": null,
            "height": 1624385
        },
        {
            "dnaid": "did:dna:Aa1XPapJHGGjHQtN2Hdyb9APv7HfiY7890",
            "companyDnaid": "did:dna:Aa1XPapJHGGjHQtN2Hdyb9APv7HfiYxtRz",
            "detail": "[{\"name\":\"img1\",\"hash\":\"e81475b25e49f2767522d332057c3e6bb1144c842dce47913dc8222927888888\",\"message\":\"\"},{\"name\":\"img2\",\"hash\":\"e81475b25e49f2767522d332057c3e6bb1144c842dce47913dc8222927999999\",\"message\":\"\"},{\"name\":\"img3\",\"hash\":\"e81475b25e49f2767522d332057c3e6bb1144c842dce47913dc8222927000000\",\"message\":\"\"}]",
            "type": "INDEX",
            "timestamp": "2019-04-23T03:49:20.000+0000",
            "timestampSign": "960ef0...",
            "filehash": "e81475b25e49f2767522d332057c3e6bb1144c842dce47913dc8222927777777",
            "txhash": "89e9ba952dc618229f7ab575c5c85863783fad26a1e65790be843481436cfe07",
            "createTime": "2019-04-23T03:49:21.000+0000",
            "updateTime": null,
            "height": 1624385
        }
    ],
    "error": 0,
    "desc": "SUCCESS",
    "version": "1.0.0",
    "action": "getExplorer"
}
```

| Field_Name | Type   | Description                   |
|:-----------|:-------|:------------------------------|
| error      | int    | 错误码                        |
| action     | String | 动作标志                      |
| desc       | String | 成功返回SUCCESS，失败返回错误描述 |
| result     | String | 成功返回记录，失败返回""
| version    | String | 版本号              


### 浏览器根据hash取证

```text
url：/api/v1/attestation/explorer/hash
method：POST
```

- 请求：

```json
{
	"hash":"111175b25e49f2767522d332057c3e6bb1144c842dce47913dc8222927999999"
}
```

| Field_Name | Type   | Description |
|:-----------|:-------|:------------|
| hash   | String | 文件hash或者交易hash   |

- 响应：

```json
{
    "result": [
        {
            "dnaid": "did:dna:Aa1XPapJHGGjHQtN2Hdyb9APv7HfiYxtRz",
            "companyDnaid": "did:dna:Aa1XPapJHGGjHQtN2Hdyb9APv7HfiYxtRz",
            "detail": "some message about the file ...",
            "type": "TEXT",
            "timestamp": "2019-04-22T07:32:57.000+0000",
            "timestampSign": "950ef......",
            "filehash": "111175b25e49f2767522d332057c3e6bb1144c842dce47913dc8222927999999",
            "txhash": "ee973d13c6ed2d8c7391223b4fb6f5c785f402d81d41b02ab7590113cbb00752",
            "createTime": "2019-04-22T07:32:57.000+0000",
            "updateTime": null,
            "height": 1621684
        },
        {
            "dnaid": "did:dna:Aa1XPapJHGGjHQtN2Hdyb9APv7HfiY1234",
            "companyDnaid": "did:dna:Aa1XPapJHGGjHQtN2Hdyb9APv7HfiYxtRz",
            "detail": "some message about the file ...",
            "type": "TEXT",
            "timestamp": "2019-04-22T07:32:24.000+0000",
            "timestampSign": "960ef......",
            "filehash": "111175b25e49f2767522d332057c3e6bb1144c842dce47913dc8222927999999",
            "txhash": "1ab4b5b2c6c89b4f1a553b7aef30c3f3ef203a323d23cd383261cc6d0df73870",
            "createTime": "2019-04-22T07:32:25.000+0000",
            "updateTime": null,
            "height": 1621682
        },
        {
            "dnaid": "did:dna:Aa1XPapJHGGjHQtN2Hdyb9APv7HfiYxtRz",
            "companyDnaid": "",
            "detail": "some message about the file ...",
            "type": "TEXT",
            "timestamp": "2019-04-22T04:22:55.000+0000",
            "timestampSign": "950ef......",
            "filehash": "111175b25e49f2767522d332057c3e6bb1144c842dce47913dc8222927999999",
            "txhash": "0437084a4f6204aad88fa1507fc13a44f83cecf44fc925692f9bc43f23e52fc3",
            "createTime": "2019-04-22T04:22:57.000+0000",
            "updateTime": null,
            "height": 1621275
        }
    ],
    "error": 0,
    "desc": "SUCCESS",
    "action": "selectByDnaidAndHash",
    "version": "1.0.0"
}
```

| Field_Name | Type   | Description                   |
|:-----------|:-------|:------------------------------|
| error      | int    | 错误码                        |
| action     | String | 动作标志                      |
| desc       | String | 成功返回SUCCESS，失败返回错误描述 |
| result     | String | 成功返回存证记录，失败返回""     |
| version    | String | 版本号                        |
  
       


## 错误码

| 返回代码  | 描述信息   | 备注                   |
|:-----------|:-------|:------------------------------|
| 0      | SUCCESS | 成功 |
| 61001      | INVALID_PARAMS | 参数错误 |
| 71001      | ONTID_EXIST | dnaid错误 |
| 71002      | ONTID_NOT_EXIST | dnaid错误 |
| 80001      | BLOCKCHAIN_ERROR | 区块链错误 |
| 100000      | INTERNAL_SERVER_ERROR | 服务器内部错误 |

