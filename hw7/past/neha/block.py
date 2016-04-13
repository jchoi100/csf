class Block:
    def __init__(self, recency=0, dirty=False):
        self.recency = recency
        self.dirty = dirty

    def __cmp__(self, block):
        if not isinstance(block, Block):
            raise ValueError("Must be of Block type.")
        else:
            if self.recency < block.recency:
                return -1
            elif self.recency > block.recency:
                return 1
            else:
                return 0
                