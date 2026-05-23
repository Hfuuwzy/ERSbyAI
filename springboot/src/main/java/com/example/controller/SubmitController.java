    /**
     * 批量更新状态
     */
    @PostMapping("/batchUpdate")
    public Result batchUpdate(@RequestBody BatchUpdateForm form) {
        submitService.batchUpdate(form);
        return Result.success();
    }
