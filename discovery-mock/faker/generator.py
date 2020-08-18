from faker import Faker

fake = Faker()
domens = ['spb', 'msk', 'nnov']
statuses = ['Active', 'OnHold', 'Unavailable']
models = ['Cisco MC1000-PX', 'Huawei MA5800C-X16',
          'TP-Link MC220L', 'D-link DMC-F02SC', '3C-Link 3C-WDM-100-35 (53)-20',
          'Osnovo OMC-100-21S5a', 'ROBOFIBER LFC-1002-SFP', 'EFB-ELEKTRONIK CM-022A', 'SNR SNR-CVT-1000SFP-I',
          'MultiCo MY-MC100', 'Cisco WS-C3750G-24TS-S1U', 'RAISECOM ISCOM2016-AC', 'Zyxel MES-3528',
          'Huawei S5328C-EI-24S', 'Raisecom MSG1200-GEC', 'D-link DSL-2640/NRU', 'D-link DI-604',
          'Zyxel GS-108BV3-EU0101F', 'Juniper T4000-12XGE-FPC5-UPG', 'Juniper ACX2000-DC', 'Cisco AIR-CT8510-1K-K9',
          'Cisco WS-C3850-48T-L']


class FakeData():
    def generate(self, items_cnt):
        cnt = int(items_cnt)
        return [Item(fake.word(ext_word_list=domens), fake.ipv4(), fake.word(ext_word_list=models),
                     fake.word(ext_word_list=statuses)) for _ in range(cnt)]


class Item():
    def __init__(self, domen, ipv4, model, status):
        self.domen = domen
        self.ipv4 = ipv4
        self.model = model
        self.status = status
