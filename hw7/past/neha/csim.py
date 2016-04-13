import sys
from math import log
from block import Block
from set import Set


class CacheSimulator:
    def __init__(self, num_sets, num_blocks, num_bytes,
                 write_allocate, write_through, lru):
        self.num_sets = num_sets
        self.num_blocks = num_blocks
        self.num_bytes = num_bytes
        self.cache = dict((i, Set()) for i in range(num_sets))
        self.write_allocate = write_allocate
        self.write_through = write_through
        self.lru = lru
        self.num_cycles, self.num_loads, self.num_stores = 0, 0, 0
        self.num_load_hits, self.num_store_hits = 0, 0
        self.num_load_misses, self.num_store_misses = 0, 0

    def index(self, address):
        return (self.num_sets - 1) & (address >> int(log2(self.num_bytes)))

    def tag(self, address):
        return address >> int(log2(self.num_bytes)) >> int(log2(self.num_sets))

    def is_hit(self, idx, tg):
        if len(self.cache[idx].blocks) > 0:
            if tg in self.cache[idx].blocks:
                return True
        return False

    def create_block(self, idx, tg):
        if len(self.cache[idx].blocks) == self.num_blocks:
            least_used_key = min(self.cache[idx].blocks,
                                 key=self.cache[idx].blocks.get)
            if self.cache[idx].blocks[least_used_key].dirty:
                self.num_cycles += 25 * self.num_bytes
            del self.cache[idx].blocks[least_used_key]
        self.cache[idx].blocks[tg] = Block(self.cache[idx].count)
        self.cache[idx].count += 1
        self.num_cycles += 25 * self.num_bytes

    def update_recency(self, idx, tg):
        self.cache[idx].blocks[tg].recency = self.cache[idx].count
        self.cache[idx].count += 1

    def load_address(self, address):
        idx, tg = self.index(address), self.tag(address)
        if self.is_hit(idx, tg):
            self.num_load_hits += 1
            if self.lru:
                self.update_recency(idx, tg)
        else:
            self.num_load_misses += 1
            self.create_block(idx, tg)
        self.num_cycles += 1
        self.num_loads += 1

    def store_address(self, address):
        idx, tg = self.index(address), self.tag(address)
        if self.write_allocate:
            if self.is_hit(idx, tg):
                if self.lru:
                    self.update_recency(idx, tg)
                self.num_store_hits += 1
            else:
                self.create_block(idx, tg)
                self.num_store_misses += 1
            self.num_cycles += 1
            if self.write_through:
                self.num_cycles += 100
            else:
                self.cache[idx].blocks[tg].dirty = True
        else:
            if self.is_hit(idx, tg):
                if self.lru:
                    self.update_recency(idx, tg)
                self.num_cycles += 1
                self.num_store_hits += 1
            else:
                self.num_store_misses += 1
            self.num_cycles += 100
        self.num_stores += 1

    def write_output(self):
        sys.stdout.write(
            "Total loads: " + str(self.num_loads) + "\n" +
            "Total stores: " + str(self.num_stores) + "\n" +
            "Load hits: " + str(self.num_load_hits) + "\n" +
            "Load misses: " + str(self.num_load_misses) + "\n" +
            "Store hits: " + str(self.num_store_hits) + "\n" +
            "Store misses: " + str(self.num_store_misses) + "\n" +
            "Total cycles: " + str(self.num_cycles) + "\n"
        )


def powerof2(n):
    return n != 0 and ((n & (n - 1)) == 0)


def log2(n):
    return log(n, 2)


def str_to_bool(s):
    if s == "0":
        return False
    elif s == "1":
        return True
    else:
        raise ValueError("Boolean defined as either 0 or 1")


def main():
    try:
        num_sets = int(sys.argv[1])
        num_blocks = int(sys.argv[2])
        num_bytes = int(sys.argv[3])
        write_allocate = str_to_bool(sys.argv[4])
        write_through = str_to_bool(sys.argv[5])
        lru = str_to_bool(sys.argv[6])
        filename = sys.argv[7]
    except:
        sys.stderr.write("Invalid command-line arguments.\n")
        sys.exit(1)

    if (num_sets > 0 and powerof2(num_sets) and
            num_blocks > 0 and powerof2(num_blocks) and
            num_bytes >= 4 and powerof2(num_bytes) and
            (write_allocate or write_through)):
        csim = CacheSimulator(num_sets, num_blocks, num_bytes,
                              write_allocate, write_through, lru)
        with open(filename) as trace:
            for line in trace.readlines():
                args = line.split()
                operation = args[0]
                address = args[1]
                if operation == 'l':
                    csim.load_address(int(address, 16))
                elif operation == 's':
                    csim.store_address(int(address, 16))
                else:
                    sys.stderr.write("Invalid input.\n")
                    sys.exit(1)
        csim.write_output()
    else:
        sys.stderr.write("Invalid command-line arguments.\n")
        sys.exit(1)

if __name__ == '__main__':
    main()
