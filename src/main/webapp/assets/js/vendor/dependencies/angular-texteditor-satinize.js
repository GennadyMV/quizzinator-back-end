!function(a,b){b["true"]=a,function(a,b){"use strict";function c(){this.$get=["$$sanitizeUri",function(a){return function(b){var c=[];return f(b,k(c,function(b,c){return!/^unsafe/.test(a(b,c))})),c.join("")}}]}function d(a){var c=[],d=k(c,b.noop);return d.chars(a),c.join("")}function e(a){var b,c={},d=a.split(",");for(b=0;b<d.length;b++)c[d[b]]=!0;return c}function f(a,c){function d(a,d,f,h){if(d=b.lowercase(d),A[d])for(;j.last()&&B[j.last()];)e("",j.last());z[d]&&j.last()==d&&e("",d),h=w[d]||!!h,h||j.push(d);var i={};f.replace(o,function(a,b,c,d,e){var f=c||d||e||"";i[b]=g(f)}),c.start&&c.start(d,i,h)}function e(a,d){var e,f=0;if(d=b.lowercase(d))for(f=j.length-1;f>=0&&j[f]!=d;f--);if(f>=0){for(e=j.length-1;e>=f;e--)c.end&&c.end(j[e]);j.length=f}}var f,h,i,j=[],k=a;for(j.last=function(){return j[j.length-1]};a;){if(h=!0,j.last()&&C[j.last()])a=a.replace(new RegExp("(.*)<\\s*\\/\\s*"+j.last()+"[^>]*>","i"),function(a,b){return b=b.replace(r,"$1").replace(t,"$1"),c.chars&&c.chars(g(b)),""}),e("",j.last());else if(0===a.indexOf("<!--")?(f=a.indexOf("--",4),f>=0&&a.lastIndexOf("-->",f)===f&&(c.comment&&c.comment(a.substring(4,f)),a=a.substring(f+3),h=!1)):s.test(a)?(i=a.match(s),i&&(a=a.replace(i[0],""),h=!1)):q.test(a)?(i=a.match(n),i&&(a=a.substring(i[0].length),i[0].replace(n,e),h=!1)):p.test(a)&&(i=a.match(m),i&&(a=a.substring(i[0].length),i[0].replace(m,d),h=!1)),h){f=a.indexOf("<");var u=0>f?a:a.substring(0,f);a=0>f?"":a.substring(f),c.chars&&c.chars(g(u))}if(a==k)throw l("badparse","The sanitizer was unable to parse the following block of html: {0}",a);k=a}e()}function g(a){if(!a)return"";var b=H.exec(a),c=b[1],d=b[3],e=b[2];return e&&(G.innerHTML=e.replace(/</g,"&lt;"),e="textContent"in G?G.textContent:G.innerText),c+e+d}function h(a){return a.replace(/&/g,"&amp;").replace(u,function(a){var b=a.charCodeAt(0),c=a.charCodeAt(1);return"&#"+(1024*(b-55296)+(c-56320)+65536)+";"}).replace(v,function(a){var b=a.charCodeAt(0);return 159>=b||173==b||b>=1536&&1540>=b||1807==b||6068==b||6069==b||b>=8204&&8207>=b||b>=8232&&8239>=b||b>=8288&&8303>=b||65279==b||b>=65520&&65535>=b?"&#"+b+";":a}).replace(/</g,"&lt;").replace(/>/g,"&gt;")}function i(a){var c="",d=a.split(";");return b.forEach(d,function(a){var d=a.split(":");if(2==d.length){var e=I(b.lowercase(d[0])),a=I(b.lowercase(d[1]));(("color"===e||"background-color"===e)&&(a.match(/^rgb\([0-9%,\. ]*\)$/i)||a.match(/^rgba\([0-9%,\. ]*\)$/i)||a.match(/^hsl\([0-9%,\. ]*\)$/i)||a.match(/^hsla\([0-9%,\. ]*\)$/i)||a.match(/^#[0-9a-f]{3,6}$/i)||a.match(/^[a-z]*$/i))||"text-align"===e&&("left"===a||"right"===a||"center"===a||"justify"===a)||"float"===e&&("left"===a||"right"===a||"none"===a)||("width"===e||"height"===e)&&a.match(/[0-9\.]*(px|em|rem|%)/))&&(c+=e+": "+a+";")}}),c}function j(a,b,c,d){return"img"===a&&b["ta-insert-video"]&&("ta-insert-video"===c||"allowfullscreen"===c||"frameborder"===c||"contenteditble"===c&&"false"===d)?!0:!1}function k(a,c){var d=!1,e=b.bind(a,a.push);return{start:function(a,f,g){a=b.lowercase(a),!d&&C[a]&&(d=a),d||D[a]!==!0||(e("<"),e(a),b.forEach(f,function(d,g){var k=b.lowercase(g),l="img"===a&&"src"===k||"background"===k;("style"===k&&""!==(d=i(d))||j(a,f,k,d)||F[k]===!0&&(E[k]!==!0||c(d,l)))&&(e(" "),e(g),e('="'),e(h(d)),e('"'))}),e(g?"/>":">"))},end:function(a){a=b.lowercase(a),d||D[a]!==!0||(e("</"),e(a),e(">")),a==d&&(d=!1)},chars:function(a){d||e(h(a))}}}var l=b.$$minErr("$sanitize"),m=/^<\s*([\w:-]+)((?:\s+[\w:-]+(?:\s*=\s*(?:(?:"[^"]*")|(?:'[^']*')|[^>\s]+))?)*)\s*(\/?)\s*>/,n=/^<\s*\/\s*([\w:-]+)[^>]*>/,o=/([\w:-]+)(?:\s*=\s*(?:(?:"((?:[^"])*)")|(?:'((?:[^'])*)')|([^>\s]+)))?/g,p=/^</,q=/^<\s*\//,r=/<!--(.*?)-->/g,s=/<!DOCTYPE([^>]*?)>/i,t=/<!\[CDATA\[(.*?)]]>/g,u=/[\uD800-\uDBFF][\uDC00-\uDFFF]/g,v=/([^\#-~| |!])/g,w=e("area,br,col,hr,img,wbr"),x=e("colgroup,dd,dt,li,p,tbody,td,tfoot,th,thead,tr"),y=e("rp,rt"),z=b.extend({},y,x),A=b.extend({},x,e("address,article,aside,blockquote,caption,center,del,dir,div,dl,figure,figcaption,footer,h1,h2,h3,h4,h5,h6,header,hgroup,hr,ins,map,menu,nav,ol,pre,script,section,table,ul")),B=b.extend({},y,e("a,abbr,acronym,b,bdi,bdo,big,br,cite,code,del,dfn,em,font,i,img,ins,kbd,label,map,mark,q,ruby,rp,rt,s,samp,small,span,strike,strong,sub,sup,time,tt,u,var")),C=e("script,style"),D=b.extend({},w,A,B,z),E=e("background,cite,href,longdesc,src,usemap"),F=b.extend({},E,e("abbr,align,alt,axis,bgcolor,border,cellpadding,cellspacing,class,clear,color,cols,colspan,compact,coords,dir,face,headers,height,hreflang,hspace,ismap,lang,language,nohref,nowrap,rel,rev,rows,rowspan,rules,scope,scrolling,shape,size,span,start,summary,target,title,type,valign,value,vspace,width")),G=document.createElement("pre"),H=/^(\s*)([\s\S]*?)(\s*)$/,I=function(){return String.prototype.trim?function(a){return b.isString(a)?a.trim():a}:function(a){return b.isString(a)?a.replace(/^\s\s*/,"").replace(/\s\s*$/,""):a}}();b.module("ngSanitize",[]).provider("$sanitize",c),b.module("ngSanitize").filter("linky",["$sanitize",function(a){var c=/((ftp|https?):\/\/|(mailto:)?[A-Za-z0-9._%+-]+@)\S*[^\s.;,(){}<>]/,e=/^mailto:/;return function(f,g){function h(a){a&&n.push(d(a))}function i(a,c){n.push("<a "),b.isDefined(g)&&(n.push('target="'),n.push(g),n.push('" ')),n.push('href="'),n.push(a),n.push('">'),h(c),n.push("</a>")}if(!f)return f;for(var j,k,l,m=f,n=[];j=m.match(c);)k=j[0],j[2]==j[3]&&(k="mailto:"+k),l=j.index,h(m.substr(0,l)),i(k,j[0].replace(e,"")),m=m.substring(l+j[0].length);return h(m),a(n.join(""))}}])}(window,window.angular)}({},function(){return this}());