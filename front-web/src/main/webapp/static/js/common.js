var webSocketManager = (function() {

    var domainURL = _gv.webSocketUrl;
    var endPoint = "/socket-io";

    var connectOptions;

    var RETRY_MAX = 6;
    var retryCount = 0;

    var isConnected = false;

    var stompClient = null;

    // BROKER_ENUM 기준으로 처리
    var subscribeTemplate = {
        INFORM: "/user/info/inform",
        NOTICE: "/user/info/notice",
        NOTICE_ALL: "/info/notice/all",
        COINSTAT: "/info/{baseCrncy}/stat",
        CHART_MIN: "/info/{baseCrncy}/chart/minutely",
        CHART_DAY: "/info/{baseCrncy}/chart/daily",
        ORDER: "/info/{baseCrncy}/order/{crncy}",
        CMPL: "/info/{baseCrncy}/cmpl/{crncy}"
    };

    var staticSubscribes = [];

    function init() {
        staticSubscribes.push(subscribeTemplate.INFORM);
        staticSubscribes.push(subscribeTemplate.NOTICE);
        staticSubscribes.push(subscribeTemplate.NOTICE_ALL);
        $.each(cmMarketList, function(i, item) {
            var baseCrncy = item.baseCrncyCd;
            var marketCrncyList = cmMarketCrncyMap[baseCrncy];
            staticSubscribes.push(subscribeTemplate.COINSTAT.replace("{baseCrncy}", baseCrncy));
            staticSubscribes.push(subscribeTemplate.CHART_MIN.replace("{baseCrncy}", baseCrncy));
            staticSubscribes.push(subscribeTemplate.CHART_DAY.replace("{baseCrncy}", baseCrncy));
            $.each(marketCrncyList, function(i, item) {
                var crncy = item.crncyCd;
                staticSubscribes.push(subscribeTemplate.ORDER.replace("{baseCrncy}", baseCrncy).replace("{crncy}", crncy));
                staticSubscribes.push(subscribeTemplate.CMPL.replace("{baseCrncy}", baseCrncy).replace("{crncy}", crncy));
            });
        });
    }

    function disconnect() {
        if(!isConnected) {
            return false;
        }
        printTxt("Disconnecting...");
        if(stompClient !== null) {
            stompClient.disconnect(disConnectCallback);
        }
    }

    function disConnectCallback() {
        printTxt("Disconnected");
    }

    function connect(options) {
        // 이미 접속한 경우, 이후 접속 차단 혹은 재접속 방법 중 선택
        if(isConnected) {
            printTxt("Already Connected!");
            return false;
        }
        if(!options) {
            options = {};
        }
        connectOptions = options;
        var socket = new SockJS(domainURL + endPoint);
        stompClient = Stomp.over(socket);
        stompClient.debug = function(str) {}; // 디버깅 콘솔 출력 제한
        stompClient.connect({memNo: options.memNo}, function(frame) {
            // callback function to be called when stomp client is connected to server
            printTxt("Connected!");
            addSubscribes(options.subscribeCallbacks);
            isConnected = true;
            // 접속 재시도 초기화
            retryCount = 0;
        }, errorCallback);
    }

    // display the error's message header:
    function errorCallback(error) {
        console.warn(error);
        isConnected = false;
        if(retryCount >= RETRY_MAX) {
            console.warn("Retry connect failed. Need to refresh.");
            return false;
        }
        setTimeout(checkConnect, 5000);
    }

    function addSubscribes(subscribeCallbacks) {
        if(!subscribeCallbacks) {
            subscribeCallbacks = {};
        }
        $.each(staticSubscribes, function(i, destination) {
            stompClient.subscribe(destination, function (result) {
                var itemData = JSON.parse(result.body);
                var statIdx = destination.indexOf("/stat");
                if(statIdx != -1) {
                    // destination(/info/{baseCrncy}/stat) 에서 baseCrncyCd 추출
                    var baseCrncyCd = destination.substring(6, statIdx);
                    commMarketCoinCtrl.updateFiatRateMap(baseCrncyCd, itemData);
                    commMarketCoinCtrl.renderMarketCoinList(baseCrncyCd, itemData);
                }
                else if(destination == subscribeTemplate.INFORM) {
                    printTxt(itemData);
                    var informMessage = "체결되었습니다.";
                    //var informMessage = itemData.dealType == "S" ? "판매 체결되었습니다.":"구매 체결되었습니다.";
                    //informMessage += "<br>(" + itemData.cmplQty + " " + itemData.crncyCd + ")";
                    commToast.info(informMessage, "체결알림");

                    // 지갑 정보 다시 조회
                    commDataViewCtrl.getCurrCashInfo();
                }
                else if(destination == subscribeTemplate.NOTICE) {
                    console.log("NOTICE");
                    console.log(itemData);
                }
                else if(destination == subscribeTemplate.NOTICE_ALL) {
                    console.log("NOTICE_ALL");
                    console.log(itemData);
                }
                var subscribeCallback = subscribeCallbacks[destination];
                if(typeof subscribeCallback === "function") {
                    subscribeCallback(itemData);
                }
            });
        });
    }

    function sendMessage(destination, paramMap) {
        if(!isConnected) {
            return false;
        }
        stompClient.send(destination, {}, JSON.stringify(paramMap));
    }

    function checkConnect() {
        if(!isConnected) {
            console.warn("Retry connect " + (retryCount + 1) + " times..");
            connect(connectOptions);
            retryCount++;
        }
        return isConnected;
    }

    function isConnect() {
        return isConnected;
    }

    return {
        init: init,
        connect: connect,
        disconnect: disconnect,
        sendMessage: sendMessage,
        checkConnect: checkConnect,
        isConnect: isConnect
    }
})();