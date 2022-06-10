if (gtagUrl) {
    $('#gtag').attr('src', gtagUrl);
    let gtagId = gtagUrl.split('id=')[1];
    console.log(gtagId);
    window.dataLayer = window.dataLayer || [];
    function gtag() { dataLayer.push(arguments); }
    gtag('js', new Date());

    gtag('config', gtagId);
}