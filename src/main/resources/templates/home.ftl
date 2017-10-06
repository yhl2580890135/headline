<#include "header.ftl" parse=true/>
<div id="main">
    <!--
        <div class="hero">
            <div class="container-fluid">
                <div class="row">
                    <div class="col-sm-6">
                        <div class="app-iphone">
                            <div class="carousel slide" data-ride="carousel" data-interval="3000">
                                <div class="carousel-inner" role="listbox">
                                    <div class="item">
                                      <img alt="牛客网应用截图阅读精选" src="http://images.nowcoder.com/images/20141231/622873_1420036789276_622873_1420036771761_%E8%98%91%E8%8F%87.jpg@0e_200w_200h_0c_1i_1o_90Q_1x">
                                      <div class="carousel-caption">阅读精选</div>
                                    </div>
                                    <div class="item active">
                                      <img alt="牛客网应用截图订阅主题" src="http://images.nowcoder.com/images/20141231/622873_1420036789276_622873_1420036771761_%E8%98%91%E8%8F%87.jpg@0e_200w_200h_0c_1i_1o_90Q_1x">
                                      <div class="carousel-caption">订阅主题</div>
                                    </div>
                                    <div class="item">
                                      <img alt="牛客网应用截图分享干货" src="http://images.nowcoder.com/images/20141231/622873_1420036789276_622873_1420036771761_%E8%98%91%E8%8F%87.jpg@0e_200w_200h_0c_1i_1o_90Q_1x">
                                      <div class="carousel-caption">分享干货</div>
                                    </div>
                                    <div class="item">
                                      <img alt="牛客网应用截图兑换礼品" src="http://images.nowcoder.com/images/20141231/622873_1420036789276_622873_1420036771761_%E8%98%91%E8%8F%87.jpg@0e_200w_200h_0c_1i_1o_90Q_1x">
                                      <div class="carousel-caption">兑换礼品</div>
                                    </div>
                                    <div class="item">
                                      <img alt="牛客网应用截图建立品牌" src="http://images.nowcoder.com/images/20141231/622873_1420036789276_622873_1420036771761_%E8%98%91%E8%8F%87.jpg@0e_200w_200h_0c_1i_1o_90Q_1x">
                                      <div class="carousel-caption">建立品牌</div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>

                    <div class="col-sm-6">
                        <div class="intro">
                            <div class="title">
                              <h1>牛客网</h1>
                              <h3>程序员必装的 App</h3>
                            </div>
                            <div class="media">
                                <div class="media-left">
                                    <img class="media-object app-qrcode" src="/images/res/qrcode.png" alt="App qrcode web index">
                                </div>
                                <div class="media-body">
                                    <div class="buttons">
                                      <p><a onclick="_hmt.push([&#39;_trackEvent&#39;, &#39;app&#39;, &#39;download&#39;, &#39;ios&#39;])" class="btn btn-success btn-lg" href="http://nowcoder.com/s/ios"><i class="fa icon-apple"></i> iPhone 版</a></p>
                                      <p><a onclick="_hmt.push([&#39;_trackEvent&#39;, &#39;app&#39;, &#39;download&#39;, &#39;apk&#39;])" class="btn btn-success btn-lg" href="http://nowcoder.com/s/apk"><i class="fa icon-android"></i> Android 版</a></p>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        -->

    <div class="container" id="daily">
        <div class="jscroll-inner">
        <div class="daily">

        <#setting date_format="yyyy-MM-dd">
        <#list vos as map>
            <#if (cur_date?date?matches(map.news.createdDate?date)) == false>
                <#if (map_index>0 )>
                </div>
                </#if>
                <h3 class="date">
                    <i class="fa icon-calendar"></i>
                    <span>头条资讯 &nbsp; ${map.news.createdDate?date}</span>
                </h3>
            <div class="posts">
                <#assign cur_date=map.news.createdDate?date/>
            </#if>
            <div class="post">
                <div class="votebar">
                    <#if (map.like gt 0)>
                        <button class="click-like up pressed" data-id="${map.news.id}" title="赞同"><i
                                class="vote-arrow"></i><span class="count">${map.news.likeCount}</span></button>
                    <#else>
                        <button class="click-like up" data-id="${map.news.id}" title="赞同"><i
                                class="vote-arrow"></i><span class="count">${map.news.likeCount}</span></button>
                    </#if>
                    <#if (map.like lt 0)>
                        <button class="click-dislike down pressed" data-id="${map.news.id}" title="反对"><i
                                class="vote-arrow"></i></button>
                    <#else>
                        <button class="click-dislike down" data-id="${map.news.id}" title="反对"><i
                                class="vote-arrow"></i></button>
                    </#if>
                </div>
                <div class="content">
                    <div>
                        <img class="content-img" src="${map.news.image}" alt="">
                    </div>
                    <div class="content-main">
                        <h3 class="title">
                            <a target="_blank" rel="external nofollow" href="/news/${map.news.id}">${map.news.title}</a>
                        </h3>
                        <div class="meta">
                        ${map.news.link}
                            <span>
                                            <i class="fa icon-comment"></i> ${map.news.commentCount}
                                        </span>
                        </div>
                    </div>
                </div>
                <div class="user-info">
                    <div class="user-avatar">
                        <a href="/user/${map.user.id}/"><img width="32" class="img-circle"
                                                             src="${map.user.headUrl}"></a>
                    </div>

                    <!--
                    <div class="info">
                        <h5>分享者</h5>

                        <a href="http://nowcoder.com/u/251205"><img width="48" class="img-circle" src="http://images.nowcoder.com/images/20141231/622873_1420036789276_622873_1420036771761_%E8%98%91%E8%8F%87.jpg@0e_200w_200h_0c_1i_1o_90Q_1x" alt="Thumb"></a>

                        <h4 class="m-b-xs">冰燕</h4>
                        <a class="btn btn-default btn-xs" href="http://nowcoder.com/signin"><i class="fa icon-eye"></i> 关注TA</a>
                    </div>
                    -->
                </div>
                <div class="subject-name">来自 <a href="/user/${map.user.id}/">${map.user.username}</a></div>
            </div>

            <!--
            <div class="alert alert-warning subscribe-banner" role="alert">
              《头条八卦》，每日 Top 3 通过邮件发送给你。      <a class="btn btn-info btn-sm pull-right" href="http://nowcoder.com/account/settings">立即订阅</a>
            </div>
            -->
            <#if (map_index==vos?size)>
            </div>
            </#if>
        </#list>


        </div>
    </div>
</div>
</div>
<#if pop??>
<script>
    window.loginpop = ${pop};
</script>
</#if>
<#include "footer.ftl" parse=true/>
