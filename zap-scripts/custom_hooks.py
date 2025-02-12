def zap_started(zap, target):
    print("ZAP started, configuring rules...")

    # 忽略警告 ID 10049
    zap.pscan.disable_scanners("10049")

    # 忽略特定 URL
    zap.spider.exclude_from_scan(".*robots\\.txt.*")
    zap.spider.exclude_from_scan(".*sitemap\\.xml.*")

    print("Configuration applied.")